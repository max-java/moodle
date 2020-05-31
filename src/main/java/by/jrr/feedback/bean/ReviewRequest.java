package by.jrr.feedback.bean;

import by.jrr.profile.bean.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
    @GeneratedValue
    private Long Id;
    private Long itemId;
    private Long reviewedEntityId; // TODO: 27/05/20 should it be duplicated here alongside with entityId in Item?
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
}
