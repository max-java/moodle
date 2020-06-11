package by.jrr.files.repository;

import by.jrr.files.bean.FileBytes;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileBytesRepository extends PagingAndSortingRepository<FileBytes, Long> {
    Optional<FileBytes> findByFileName(String fileName); // TODO: 04/06/20 replace wih optional

}
