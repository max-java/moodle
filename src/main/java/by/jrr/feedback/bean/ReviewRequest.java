package by.jrr.feedback.bean;

import by.jrr.profile.bean.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest implements Cloneable{

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;
    private Long itemId;
    private Long reviewedEntityId; // TODO: 27/05/20 should it be duplicated here alongside with entityId in Item? 06/06/20 Well, it duplicated, but it quite handy.
    private Long requesterProfileId;
    @Lob
    private String requesterNotes;
    private String link;

    @CreatedDate
    private LocalDateTime createdDate;
    private LocalDateTime closedDate;
    @Enumerated(value = EnumType.STRING)
    private ReviewResult reviewResultOnClosing;

    @Transient
    private List<Review> reviews;
    @Transient
    private Item item;
    @Transient
    private Profile requesterProfile;


    // TODO: 11/07/20 conciser which fields are required and add @NotNull validation
    // TODO: 11/07/20 Pair.of has @NotNull first and @NotNull seconds, which throws exception
    // TODO: 11/07/20 see ReviewRequestPageableSearchService.searchReviewRequestByAllReviewRequestFields();
    public String addLinkToSearch() {
        String result = new String();
        try {
            result = this.getLink();
        } catch (Exception ex) {
            return new String();
        }
        return result == null ? new String() : result;
    }

    public String addReviewdItemTypeNameToSearch() {
        String result = new String();
        try {
            result = this.getItem().getReviewedItemType().name();
        } catch (Exception ex) {
            return new String();
        }
        return result == null ? new String() : result;
    }

    public String addReviewdEntityNameToSearch() {
        String result = new String();
        try {
            result = this.getItem().getReviewedEntity().getName();
        } catch (Exception ex) {
            return new String();
        }
        return result == null ? new String() : result;
    }

    public String addRequesterFullUserNameToSearch() {
        String result = new String();
        try {
            result = this.getRequesterProfile().getUser().getFullUserName();
        } catch (Exception ex) {
            return new String();
        }
        return result == null ? new String() : result;
    }

    public String addRequesterNotesToSearch() {
        String result = new String();
        try {
            result = this.getRequesterNotes();
        } catch (Exception ex) {
            return new String();
        }
        return result == null ? new String() : result;
    }

    public String addReviewResultOnClosingSearch() {
        String result = new String();
        try {
            result = this.getReviewResultOnClosing().name();
        } catch (Exception ex) {
            return new String();
        }
        return result == null ? new String() : result;
    }

}
