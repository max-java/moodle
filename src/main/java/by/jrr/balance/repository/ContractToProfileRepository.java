package by.jrr.balance.repository;


import by.jrr.balance.bean.ContractToProfile;
import by.jrr.balance.bean.OperationToProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by ideapad on 27.9.17.
 */
@Repository
public interface ContractToProfileRepository extends CrudRepository<ContractToProfile, Long> {

    List<ContractToProfile> findAllByStreamId(Long id);
    Optional<ContractToProfile> findByContractId(Long id);
    List<ContractToProfile> findAllBySubscriberId(Long id);

}
