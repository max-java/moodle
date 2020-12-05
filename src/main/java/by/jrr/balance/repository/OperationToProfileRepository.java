package by.jrr.balance.repository;


import by.jrr.balance.bean.OperationToProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by ideapad on 27.9.17.
 */
@Repository
public interface OperationToProfileRepository extends CrudRepository<OperationToProfile, Long> {

    List<OperationToProfile> findAllByStreamId(Long id);
    List<OperationToProfile> findAllByContractId(Long id);
    List<OperationToProfile> findAllByContractIdIsNull();
    List<OperationToProfile> findAllBySubscriberId(Long id);
    Optional<OperationToProfile> findByOperationRowId(Long id);

}
