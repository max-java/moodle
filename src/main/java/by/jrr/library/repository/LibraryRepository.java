package by.jrr.library.repository;


import by.jrr.library.bean.MyBook;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryRepository extends PagingAndSortingRepository<MyBook, Long> {

}
