package by.jrr.statistic.bean;

import by.jrr.feedback.bean.EntityType;
import by.jrr.profile.bean.Profile;
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
public class UserProgress {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;
    private Long profileId;

    private Long trackableId;
    @Enumerated(value = EnumType.STRING)
    private EntityType trackableType;
    private String trackableName;
    private String trackableLink;
    @Enumerated(value = EnumType.STRING)
    private TrackStatus trackStatus;



    @Transient
    private Trackable trackable;
    @Transient
    private Profile profile;

}
