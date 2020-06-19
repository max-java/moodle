package by.jrr.library.repository;

import by.jrr.library.bean.MyBook;
import by.jrr.moodle.bean.PracticeQuestion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends PagingAndSortingRepository<MyBook, Long> {
    Streamable<MyBook> findByNameContaining(String name);
}
