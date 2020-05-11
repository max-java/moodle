package by.jrr.project.repository;

import by.jrr.project.bean.Issue;
import by.jrr.project.bean.Project;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends PagingAndSortingRepository<Issue, Long> {
}
