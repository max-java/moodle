package by.jrr.profile.bean;

import by.jrr.auth.bean.User;
import by.jrr.moodle.bean.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Lob;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    // TODO: 10/06/20 Несмотря на то, что профиль пользователя, команда и стрим используют одну и туже вьюху,
    // TODO: 10/06/20 для работы с ними нужно три раздельные сущности


    @javax.persistence.Id
    @GeneratedValue
    private Long Id;
    @Transient
    private User user;
    private long userId;

    private String avatarFileName;
    private Long ownerProfileId;

    @Transient
    private List<StreamAndTeamSubscriber> subscribers = new ArrayList<>();

    // that is only for streams
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private Boolean isEnrolOpen;
    private Long courseId;
    @Transient
    private Course course;
}
