package by.jrr.project.service;

import by.jrr.auth.bean.User;
import by.jrr.auth.service.UserService;
import by.jrr.project.bean.Issue;
import by.jrr.project.bean.Project;
import by.jrr.project.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class IssueService {

    @Autowired
    IssueRepository issueRepository;
    @Autowired
    UserService userService;
    @Autowired
    ProjectService projectService;

    public Page<Issue> findAll(String page, String items) {
        Page<Issue> issuePage;
        try {
            issuePage = issueRepository.findByLastInHistory(true, PageRequest.of(Integer.valueOf(page), Integer.valueOf(items)));
            issuePage.forEach(this::setUsersAndProjectToIssue);
        } catch (Exception ex) {
            issuePage = issueRepository.findByLastInHistory(true, PageRequest.of(Integer.valueOf(0), Integer.valueOf(10)));
            issuePage.forEach(this::setUsersAndProjectToIssue);
        }
        return issuePage;
    }
    public Page<Issue> findAllByProjectIdPageable(Long projectId, String page, String items) {
        Page<Issue> issuePage;
        try {
            issuePage = issueRepository.findByProjectIdAndLastInHistory(projectId,true, PageRequest.of(Integer.valueOf(page), Integer.valueOf(items)));
            issuePage.forEach(this::setUsersAndProjectToIssue);
        } catch (Exception ex) {
            issuePage = issueRepository.findByProjectIdAndLastInHistory(projectId, true, PageRequest.of(Integer.valueOf(0), Integer.valueOf(10)));
            issuePage.forEach(this::setUsersAndProjectToIssue);
        }
        return issuePage;
    }
    public List<Issue> findAllByProjectId(Long projectId) {
        List<Issue> issuePage = new ArrayList<>();
        try {
            issuePage = issueRepository.findByProjectIdAndLastInHistory(projectId,true);
            issuePage.forEach(this::setUsersAndProjectToIssue);
        } catch (Exception ex) {
            issuePage = issueRepository.findByProjectIdAndLastInHistory(projectId, true);
            issuePage.forEach(this::setUsersAndProjectToIssue);
        }
        return issuePage;
    }


    public Issue createOrUpdate(Issue issue) {
        //issue could not be updated, only new record with new data storied to enable history
        //find last history record for this issue and set that it is not the last
        if (issue.getIssueId() != null) {
            Optional<Issue> lastRecordOpt = this.findByIssueId(issue.getIssueId());
            if (lastRecordOpt.isPresent()) {
                Issue lastRecord = lastRecordOpt.get();
                lastRecord.setLastInHistory(false);
                issueRepository.save(lastRecord);
            }
        }
        issue.setId(null); //issue could not be updated, only new record with new data storied to enable history
        issue.setTimeStamp(LocalDateTime.now()); // history enabled by setting timestamp
        issue.setLastInHistory(true); // set that it is last record in history
        issue = issueRepository.save(issue);
        if (issue.getIssueId() == null) { //if it is first history record, that issue id set from id value
            issue.setIssueId(issue.getId());
            issueRepository.save(issue);
        }
        return issue;
    }

    public void delete() {
    }

    public Optional<Issue> findByIssueId(Long issueId) {
        List<Issue> issueList = issueRepository.findByIssueId(issueId); //it returns all history for issue, so I need to pick up only last one
        if (issueList != null) {
            Optional<Issue> issue = issueList.stream().max(Comparator.comparing(Issue::getTimeStamp));
            if (issue.isPresent()) {
                issue.get().setHistory(issueList);
                issue = this.setUsersAndProjectToIssue(issue);
            }
            return issue;
        } else {
            return Optional.ofNullable(null);
        }
    }

    public Optional<List<Issue>> findHistoryByIssueId(Long issueId) {
        return Optional.ofNullable(issueRepository.findByIssueId(issueId));
    }

    private Optional<Issue> setUsersAndProjectToIssue(Optional<Issue> issue) {
        if (issue.isPresent()) {
            if (issue.get().getAssigneeUserId() != null) {
                issue.get().setAssignee(userService.findUserById(issue.get().getAssigneeUserId()).orElse(new User()));
            }
            if (issue.get().getSubmitterUserId() != null) {
                issue.get().setSubmitter(userService.findUserById(issue.get().getSubmitterUserId()).orElse(new User()));
            }
            if (issue.get().getProjectId() != null) {
                issue.get().setProject(projectService.findById(issue.get().getProjectId()).orElse(new Project()));
            }
        }
        return issue;
    }
    private Issue setUsersAndProjectToIssue(Issue issue) {

            if (issue.getAssigneeUserId() != null) {
                issue.setAssignee(userService.findUserById(issue.getAssigneeUserId()).orElse(new User()));
            }
            if (issue.getSubmitterUserId() != null) {
                issue.setSubmitter(userService.findUserById(issue.getSubmitterUserId()).orElse(new User()));
            }
            if (issue.getProjectId() != null) {
                issue.setProject(projectService.findById(issue.getProjectId()).orElse(new Project()));
            }

        return issue;
    }


}
