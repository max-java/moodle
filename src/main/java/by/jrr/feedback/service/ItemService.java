package by.jrr.feedback.service;

import by.jrr.feedback.bean.EmptyReviewable;
import by.jrr.feedback.bean.Item;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.bean.Reviewable;
import by.jrr.feedback.repository.ItemRepository;
import by.jrr.moodle.bean.PracticeQuestion;
import by.jrr.moodle.service.PracticeQuestionService;
import by.jrr.portfolio.bean.Subject;
import by.jrr.portfolio.service.SubjectService;
import by.jrr.project.bean.Issue;
import by.jrr.project.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Optional;


/**
 * get Reviewed methods should be added here for every reviewable to avoid NPE on
 * org.thymeleaf.exceptions.TemplateProcessingException: Exception evaluating SpringEL expression:
 * "item.getReviewedEntity().getName()" (template: "feedback/codeReviewRequest" - line 27, col 52)
 */

@Controller
public class ItemService {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    IssueService issueService;
    @Autowired
    PracticeQuestionService practiceQuestionService;
    @Autowired
    SubjectService subjectService;

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

    // TODO: 30/05/20 MAKE THIS PLACE MORE OBVIOUS TO TRACK!!!! LOG OR DESCRIBE IT OR PUT IN ANOTHER CLASS!!!
    // TODO: 30/05/20 consider to make similar getReviewableByReviewableId & setReviewedEntity
    private void setReviewedEntity(Item item) { // TODO: 28/05/20 replace with immutable objects
        switch (item.getReviewedItemType()) {
            case ISSUE:
                Optional<Issue> issue = issueService.findByIssueId(item.getReviewedEntityId());
                item.setReviewedEntity(issue.orElseGet(Issue::new));
                break;
            case PRACTICE_QUESTION:
                Optional<PracticeQuestion> practiceQuestion = practiceQuestionService.findById(item.getReviewedEntityId());
                item.setReviewedEntity(practiceQuestion.orElseGet(PracticeQuestion::new));
                break;
            case SUBJECT:
                Optional<Subject> subject = subjectService.findBySubjectId(item.getReviewedEntityId());
                item.setReviewedEntity(subject.orElseGet(Subject::new));
                break;

        }
    }

    // TODO: 30/05/20 consider to make similar getReviewableByReviewableId & setReviewedEntity
    public Reviewable getReviewableByReviewableId(Long reviewedEntityId) {
        Optional<Issue> issue = issueService.findByIssueId(reviewedEntityId);
        if (issue.isPresent()) {
            return issue.get();
        }
        Optional<PracticeQuestion> pq = practiceQuestionService.findById(reviewedEntityId);
        if (pq.isPresent()) {
            return pq.get();
        }
        Optional<Subject> subject = subjectService.findBySubjectId(reviewedEntityId);
        if (subject.isPresent()) {
            return subject.get();
        }
        return new EmptyReviewable();
    }
}
