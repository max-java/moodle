package by.jrr.feedback.bean;

import by.jrr.profile.bean.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;
    private Long itemId;
    private Long reviewedEntityId; // TODO: 27/05/20 should it be duplicated here alongside with entityId in Item?
    private Long reviewRequestId;
    private Long reviewerProfileId;
    @Enumerated(value = EnumType.STRING)
    private ReviewResult reviewResult;
    @Lob
    private String reviewerNotes;

    @CreatedDate
    private LocalDateTime createdDate;

    @Transient
    private Profile reviewerProfile;


}
