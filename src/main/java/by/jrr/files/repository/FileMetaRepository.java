package by.jrr.files.repository;

import by.jrr.files.bean.FileMeta;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetaRepository extends PagingAndSortingRepository<FileMeta, Long> {

}
