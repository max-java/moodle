package by.jrr.feedback.repository;

import by.jrr.feedback.bean.EntityType;
import by.jrr.feedback.bean.Item;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {
    Optional<Item> findByReviewedEntityId(Long entityId);
    Optional<Item> findByReviewedEntityIdAndReviewedItemType(Long reviewedEntityId, EntityType reviewedEntityType);
}
