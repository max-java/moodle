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
public class Item {

    @javax.persistence.Id
    @GeneratedValue
    private Long Id;
    @Enumerated(value = EnumType.STRING)
    private Long reviewedEntityId;
    private ReviewedEntityType reviewedItemType;

    @Transient
    private List<ReviewRequest> reviewRequests;
    @Transient
    private List<Review> reviews;
}
