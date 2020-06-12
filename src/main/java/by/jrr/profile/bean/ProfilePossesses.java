package by.jrr.profile.bean;

import by.jrr.feedback.bean.EntityType;
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
public class ProfilePossesses {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;
    @Transient
    private Profile profile;
    private Long profileId;

    @Enumerated(value = EnumType.STRING)
    private EntityType entityType;
    @Column(unique = true)
    private Long entityId;
}
