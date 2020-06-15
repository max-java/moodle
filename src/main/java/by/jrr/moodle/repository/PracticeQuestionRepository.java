package by.jrr.moodle.repository;

import by.jrr.interview.bean.QAndA;
import by.jrr.moodle.bean.PracticeQuestion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeQuestionRepository extends PagingAndSortingRepository<PracticeQuestion, Long> {
    Streamable<PracticeQuestion> findByNameContaining(String name);
    Streamable<PracticeQuestion> findBySummaryContaining(String name);
    Streamable<PracticeQuestion> findByThemeContaining(String name);
    Streamable<PracticeQuestion> findByDescriptionContaining(String name);
    Streamable<PracticeQuestion> findByReproStepsContaining(String name);
}
