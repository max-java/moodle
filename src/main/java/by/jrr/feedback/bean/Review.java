package by.jrr.feedback.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @javax.persistence.Id
    @GeneratedValue
    private Long Id;
    private Long itemId;
    private Long reviewedEntityId; // TODO: 27/05/20 should it be duplicated here alongside with entityId in Item?
    @Enumerated(value = EnumType.STRING)
    private Long reviewerProfileId;
    @Enumerated(value = EnumType.STRING)
    private ReviewResult reviewResult;
    @Lob
    private String reviewerNotes;
}
