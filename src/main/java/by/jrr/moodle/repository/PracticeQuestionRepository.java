package by.jrr.moodle.repository;

import by.jrr.moodle.bean.PracticeQuestion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeQuestionRepository extends PagingAndSortingRepository<PracticeQuestion, Long> {

}
