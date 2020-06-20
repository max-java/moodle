package by.jrr.library.repository;

import by.jrr.library.bean.MyBook;
import by.jrr.moodle.bean.PracticeQuestion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends PagingAndSortingRepository<MyBook, Long> {
    Streamable<MyBook> findByNameContaining(String name);
    Streamable<MyBook> findByImgContaining(String name);
    Streamable<MyBook> findByAuthorContaining(String name);
    Streamable<MyBook> findByEditionContaining(String name);
    Streamable<MyBook> findByPublisherContaining(String name);
    Streamable<MyBook> findByPublishedContaining(String name);
    Streamable<MyBook> findByIsbnContaining(String name);
}
