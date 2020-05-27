package by.jrr.feedback.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {

    @javax.persistence.Id
    @GeneratedValue
    private Long Id;
    private Long itemId;
    private Long reviewedEntityId; // TODO: 27/05/20 should it be duplicated here alongside with entityId in Item?
    private Long requesterProfileId;
    @Lob
    private String requesterNotes;
    private String link;

    @Transient
    private List<Review> reviews;
}
