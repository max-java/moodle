package by.jrr.portfolio.bean;

import by.jrr.auth.bean.User;
import by.jrr.feedback.bean.EntityType;
import by.jrr.feedback.bean.Reviewable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This is Issue class analog for user outer pages and articles
 * */

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject implements Reviewable {

    @javax.persistence.Id
    @GeneratedValue
    private Long Id;
    private Long subjectId; //it has two ids: on for issue, one for row - to create a history
    @Transient
    private Domain domain = new Domain(); // TODO: 11/05/20 handle npe otherwise
    private Long domainId;
    @Enumerated(value = EnumType.STRING)
    private SubjectType subjectType;
    @Enumerated(value = EnumType.STRING)
    private SubjectStatus subjectStatus;
    private String name;
    private String summary;
    @Lob
    private String description;
    @Lob
    private String reproSteps;
    @Transient
    private Subject parentSubject;
    private Long parentId;
    @Transient
    private User assignee = new User(); // TODO: 11/05/20 handle npe otherwise
    private Long assigneeUserId;
    private LocalDateTime timeStamp;
    @Transient
    private User submitter = new User(); // TODO: 11/05/20 handle npe otherwise
    private Long submitterUserId;
    private boolean lastInHistory;
    @Transient
    private List<Subject> history = new ArrayList<>();

    @Override
    public EntityType getType() {
        return EntityType.SUBJECT;
    }
}
