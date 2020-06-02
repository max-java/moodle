package by.jrr.portfolio.repository;

import by.jrr.portfolio.bean.Domain;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainRepository extends PagingAndSortingRepository<Domain, Long> {
}
