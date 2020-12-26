package by.jrr.feedback.service;

import by.jrr.feedback.bean.Item;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.repository.ReviewRequestRepository;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class ReviewRequestPageableSearchService {

    @Autowired
    ReviewRequestRepository reviewRequestRepository;
    @Autowired
    ItemService itemService;
    @Autowired
    ProfileService profileService;

    private final Supplier<Integer> DEFAULT_PAGE_NUMBER = () -> 1;
    private final Supplier<Integer> DEFAULT_ELEMENTS_PER_PAGE = () -> 15;


    public Page<ReviewRequest> findAllReviewRequestPageable(Optional<Integer> userFriendlyNumberOfPage,
                                                            Optional<Integer> numberOfElementsPerPage,
                                                            Optional<String> searchTerm) {

        // pages are begins from 0, but userFriendly is to begin from 1
        int page = userFriendlyNumberOfPage.orElseGet(DEFAULT_PAGE_NUMBER) - 1;
        int elem = numberOfElementsPerPage.orElseGet(DEFAULT_ELEMENTS_PER_PAGE);
        if(searchTerm.isPresent()) {
            List<ReviewRequest> reviewRequestList = searchReviewRequestByAllReviewRequestFields(searchTerm.get());
            if (reviewRequestList.size()!= 0) {
                reviewRequestList.sort(Comparator.comparing(ReviewRequest::getCreatedDate));

                // TODO: 26/05/20 this pagination should be moved in a static method
                Pageable pageable = PageRequest.of(page, elem);
                int pageOffset = (int) pageable.getOffset(); // TODO: 26/05/20 dangerous cast!
                int toIndex = (pageOffset + elem) > reviewRequestList.size() ? reviewRequestList.size() : pageOffset + elem;
                Page<ReviewRequest> rrPageImpl  = new PageImpl<>(reviewRequestList.subList(pageOffset, toIndex), pageable, reviewRequestList.size());
                return rrPageImpl;
            }
            return Page.empty();
        }

        Page<ReviewRequest> reviewRequestPage = reviewRequestRepository.findAll(PageRequest.of(page, elem)); // TODO: 26/05/20 test for NPE
        reviewRequestPage.forEach(rr -> setDataToReviewRequest(rr));
        return reviewRequestPage;
    }

    private void setDataToReviewRequest(ReviewRequest rr) {
        setItemToReviewRequest(rr);
        setRequesterProfileToReviewRequest(rr);
        setReviewedEntity(rr);
    }

    private List<ReviewRequest> searchReviewRequestByAllReviewRequestFields(String searchTerm) {
        Iterable<ReviewRequest> reviewRequests = reviewRequestRepository.findAll(); //find all requests

        reviewRequests.forEach(rr -> setItemToReviewRequest(rr));  //set data to requests
        reviewRequests.forEach(rr -> setRequesterProfileToReviewRequest(rr));
        reviewRequests.forEach(rr -> setReviewedEntity(rr));

        Iterator<ReviewRequest> reviewRequestIterator = reviewRequests.iterator(); //iterate and select pairs of searching fields and requests ids
        List<Pair<Long, String>> allFields = new ArrayList<>();
        while (reviewRequestIterator.hasNext()) {
            try {
                ReviewRequest reviewRequest = reviewRequestIterator.next(); // TODO: 28/05/20 add more fields
                allFields.add(Pair.of(reviewRequest.getId(), reviewRequest.addLinkToSearch()));
                allFields.add(Pair.of(reviewRequest.getId(), reviewRequest.addReviewdItemTypeNameToSearch()));
                allFields.add(Pair.of(reviewRequest.getId(), reviewRequest.addReviewdEntityNameToSearch()));
                allFields.add(Pair.of(reviewRequest.getId(), reviewRequest.addRequesterFullUserNameToSearch()));
                allFields.add(Pair.of(reviewRequest.getId(), reviewRequest.addRequesterNotesToSearch()));
                allFields.add(Pair.of(reviewRequest.getId(), reviewRequest.addReviewResultOnClosingSearch()));

            } catch (Exception ex) {
                // TODO: 11/07/20 handle and log exception
                // TODO: 11/07/20 java.lang.IllegalArgumentException: second is marked non-null but is null
            }

        }

        Set<Long> selectedIds = allFields.stream() //select all ids where search term is present
                .filter(a -> a.getSecond().contains(searchTerm))
                .map(a -> a.getFirst())
                .collect(Collectors.toSet());

        Iterator<ReviewRequest> reviewRequestIterator2 = reviewRequests.iterator(); // iterate and select reviewRequests matching selected ids
        List<ReviewRequest> selectedReviewRequests = new ArrayList<>();
        while (reviewRequestIterator2.hasNext()) { // TODO: 28/05/20 replace with java8 style
            ReviewRequest reviewRequest = reviewRequestIterator2.next();
            for(Long id : selectedIds) {
                if (id.equals(reviewRequest.getId())) {
                    selectedReviewRequests.add(reviewRequest);
                    break;
                }
            }
        }

        return selectedReviewRequests;
    }

    private void setItemToReviewRequest(ReviewRequest reviewRequest) {
        reviewRequest.setItem(itemService.itemRepository.findById(reviewRequest.getItemId()).orElseGet(Item::new));
    }
    private void setRequesterProfileToReviewRequest(ReviewRequest reviewRequest) {
        reviewRequest.setRequesterProfile(profileService.findProfileByProfileId(reviewRequest.getRequesterProfileId()).orElseGet(Profile::new));
    }
    private void setReviewedEntity(ReviewRequest reviewRequest) {
        reviewRequest.getItem().setReviewedEntity(itemService.getReviewableByReviewableId(reviewRequest.getItem().getReviewedEntityId()));
    }

}
