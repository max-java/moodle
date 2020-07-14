package by.jrr.telegram.bot.repository;

import by.jrr.telegram.bot.bean.NerdTermLibrary;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NerdTermLibraryRepository extends PagingAndSortingRepository<NerdTermLibrary, Long>{
    Optional<NerdTermLibrary> findByTerm(String term);
}

