package by.jrr.feedback.service;

import by.jrr.feedback.bean.EmptyReviewable;
import by.jrr.feedback.bean.Item;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.bean.Reviewable;
import by.jrr.feedback.repository.ItemRepository;
import by.jrr.project.bean.Issue;
import by.jrr.project.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class ItemService {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    IssueService issueService;

    public Item getItemByReviewable(Reviewable reviewedEntity) {
        return itemRepository
                .findByReviewedEntityId(reviewedEntity.getId())
                .orElseGet(() -> createAndSaveItem(reviewedEntity));
    }

    public Item getItemByReviewRequest(ReviewRequest reviewRequest) {
        Optional<Item> item = itemRepository.findById(reviewRequest.getItemId());
        try {
            Item myItem = item.get();
            setReviewedEntity(myItem);
            return myItem;
        } catch (NullPointerException ex) {
            ex.printStackTrace(); // TODO: 28/05/20 log to database
        }
        return new Item();
    }

    private Item createAndSaveItem(Reviewable reviewedEntity) {
        Item item = new Item();
        item.setReviewedEntityId(reviewedEntity.getId());
        item.setReviewedItemType(reviewedEntity.getType());
        return itemRepository.save(item);
    }
    private void setReviewedEntity(Item item) { // TODO: 28/05/20 replace with immutable objects
        switch (item.getReviewedItemType()) {
            case ISSUE:
                Optional<Issue> issue = issueService.findByIssueId(item.getReviewedEntityId());
                item.setReviewedEntity(issue.orElseGet(Issue::new));
                break;
        }
    }

    public Reviewable getReviewableByReviewableId(Long reviewedEntityId) {
        Optional<Issue> issue = issueService.findByIssueId(reviewedEntityId);
        if (issue.isPresent()) {
            return issue.get();
        }
        return new EmptyReviewable();
    }
}
