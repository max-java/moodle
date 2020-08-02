package by.jrr.crm.repository;

import by.jrr.crm.bean.NoteItem;
import org.omg.CORBA.portable.Streamable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteItemRepository extends PagingAndSortingRepository<NoteItem, Long> {
    List<NoteItem> findByProfileId(Long id);
}
