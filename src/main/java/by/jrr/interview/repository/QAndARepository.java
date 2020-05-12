package by.jrr.interview.repository;

import by.jrr.interview.bean.QAndA;
import by.jrr.moodle.bean.Topic;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QAndARepository extends PagingAndSortingRepository<QAndA, Long> {
}
