package by.jrr.project.repository;

import by.jrr.project.bean.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends PagingAndSortingRepository<Issue, Long> {
    List<Issue> findByIssueId(Long issueId);
    Page<Issue> findByLastInHistory(boolean isLast, Pageable var1);
    Page<Issue> findByProjectIdAndLastInHistory(Long projectId, boolean isLast, Pageable var1);
    List<Issue> findByProjectIdAndLastInHistory(Long projectId, boolean isLast);
}
