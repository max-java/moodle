package by.jrr.interview.repository;

import by.jrr.interview.bean.QAndA;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface QAndARepository extends PagingAndSortingRepository<QAndA, Long> {

    Streamable<QAndA> findByThemeContaining(String userName);
    Streamable<QAndA> findByQuestionContaining(String email);
    Streamable<QAndA> findByAnswerContaining(String name);

}
