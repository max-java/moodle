package by.jrr.telegram.bot.repository;

import by.jrr.telegram.model.TgUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TgUserRepository extends PagingAndSortingRepository<TgUser, Long>{

}

