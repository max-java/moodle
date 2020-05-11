package by.jrr.project.bean;

import by.jrr.user.profile.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Issue {

    @javax.persistence.Id
    @GeneratedValue
    private Long Id;
    private Long issueId;
    private Long projectId;
    @Enumerated(value = EnumType.STRING)
    private IssueType issueType;
    @Enumerated(value = EnumType.STRING)
    private IssueStatus issueStatus;
    private String name;
    private String summary;
    private String description;
    private String reproSteps;
    @Transient
    private Issue parentIssue;
    private Long parentId;
    @Transient
    private Profile assignee;
    private Long assigneeUserId;
    private LocalDateTime timeStamp;
    @Transient
    private Profile submitter;
    private Long submitterUserId;

}
