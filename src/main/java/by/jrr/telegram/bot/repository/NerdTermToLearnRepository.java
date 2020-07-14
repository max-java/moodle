package by.jrr.telegram.bot.repository;

import by.jrr.telegram.bot.bean.NerdTermToLearn;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NerdTermToLearnRepository extends PagingAndSortingRepository<NerdTermToLearn, Long>{

}

