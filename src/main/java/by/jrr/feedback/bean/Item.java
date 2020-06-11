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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;
    @Column(unique=true)
    private Long reviewedEntityId;
    @Enumerated(value = EnumType.STRING)
    private EntityType reviewedItemType;

    @Transient
    private Reviewable reviewedEntity;
    @Transient
    private List<ReviewRequest> reviewRequests;
    @Transient
    private List<Review> reviews;
}
