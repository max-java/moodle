package by.jrr.crm.repository;

import by.jrr.crm.bean.HistoryItem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryItemRepository extends PagingAndSortingRepository<HistoryItem, Long> {
    List<HistoryItem> findByProfileId(Long id);
}
