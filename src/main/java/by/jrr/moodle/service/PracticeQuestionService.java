package by.jrr.moodle.service;

import by.jrr.auth.service.UserService;
import by.jrr.feedback.bean.EntityType;
import by.jrr.moodle.bean.PracticeQuestion;
import by.jrr.moodle.repository.PracticeQuestionRepository;
import by.jrr.profile.service.ProfilePossessesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class PracticeQuestionService {

    private final Supplier<Integer> DEFAULT_PAGE_NUMBER = () -> 1;
    private final Supplier<Integer> DEFAULT_ELEMENTS_PER_PAGE = () -> 15;

    @Autowired
    PracticeQuestionRepository practiceQuestionRepository;
    @Autowired
    UserService userService;
    @Autowired
    ProfilePossessesService pss;


    public Page<PracticeQuestion> findAll(String page, String items) {
        Page<PracticeQuestion> topics;
        try {
            topics = practiceQuestionRepository.findAll(PageRequest.of(Integer.valueOf(page), Integer.valueOf(items)));
        } catch (Exception ex) {
            topics = practiceQuestionRepository.findAll(PageRequest.of(Integer.valueOf(0), Integer.valueOf(10)));
        }
        return topics;
    }

    public PracticeQuestion create(PracticeQuestion topic) {
        topic = practiceQuestionRepository.save(topic);
        pss.savePossessForCurrentUser(topic.getId(), EntityType.PRACTICE_QUESTION);
        return topic;
    }
    public PracticeQuestion update(PracticeQuestion topic) {
        topic = practiceQuestionRepository.save(topic);
        return topic;
    }
    public void delete() {
    }
    public Optional<PracticeQuestion> findById(Long id) {
        return practiceQuestionRepository.findById(id);
    }


    public Page<PracticeQuestion> findAllPageable(Optional<Integer> userFriendlyNumberOfPage,
                                                  Optional<Integer> numberOfElementsPerPage,
                                                  Optional<String> searchTerm) {
        // pages are begins from 0, but userFriendly is to begin from 1
        int page = userFriendlyNumberOfPage.orElseGet(DEFAULT_PAGE_NUMBER) - 1;
        int elem = numberOfElementsPerPage.orElseGet(DEFAULT_ELEMENTS_PER_PAGE);

        if(searchTerm.isPresent()) {
            return searchByAllFieldsPageable(searchTerm.get(), page, elem);
        } else {
            return practiceQuestionRepository.findAll(PageRequest.of(page, elem));
        }
    }

    private Page<PracticeQuestion> searchByAllFieldsPageable(String searchTerm, int page, int elem) {
        List<PracticeQuestion> pqList = serarchQAndAByAllFields(searchTerm);
        if(pqList.size() != 0) {
            // TODO: 26/05/20 this pagination should be moved in a static method
            Pageable pageable = PageRequest.of(page, elem);
            int pageOffset = (int) pageable.getOffset(); // TODO: 26/05/20 dangerous cast!
            int toIndex = (pageOffset + elem) > pqList.size() ? pqList.size() : pageOffset + elem;
            Page<PracticeQuestion> qAndAPageImpl  = new PageImpl<>(pqList.subList(pageOffset, toIndex), pageable, pqList.size());
            return qAndAPageImpl;
        } else {
            return Page.empty();
        }
    }

    private List<PracticeQuestion> serarchQAndAByAllFields(String searchTerm) {
        return practiceQuestionRepository.findByNameContaining(searchTerm)
                .and(practiceQuestionRepository.findByThemeContaining(searchTerm)
                .and(practiceQuestionRepository.findBySummaryContaining(searchTerm)
                .and(practiceQuestionRepository.findByDescriptionContaining(searchTerm)
                .and(practiceQuestionRepository.findByReproStepsContaining(searchTerm)))))
                .stream()
                .distinct()// TODO: 15/06/20 не дистинктит. переписать equals & hashcode?
                .collect(Collectors.toList());
    }
}
