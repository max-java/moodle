package by.jrr.portfolio.repository;

import by.jrr.portfolio.bean.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends PagingAndSortingRepository<Subject, Long> {
    List<Subject> findBySubjectId(Long subjectId);
    Page<Subject> findByLastInHistory(boolean isLast, Pageable var1);
    Page<Subject> findByDomainIdAndLastInHistory(Long domainId, boolean isLast, Pageable var1);
    List<Subject> findByDomainIdAndLastInHistory(Long domainId, boolean isLast);
}
