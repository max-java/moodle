package by.jrr.library.repository;

import by.jrr.library.bean.Book;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    Streamable<Book> findByNameContaining(String name);
    Streamable<Book> findByImgContaining(String name);
    Streamable<Book> findByAuthorContaining(String name);
    Streamable<Book> findByEditionContaining(String name);
    Streamable<Book> findByPublisherContaining(String name);
    Streamable<Book> findByPublishedContaining(String name);
    Streamable<Book> findByIsbnContaining(String name);
}
