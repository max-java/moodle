package by.jrr.balance.repository;


import by.jrr.balance.bean.ContractToOperationRow;
import by.jrr.balance.bean.ContractToProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by ideapad on 27.9.17.
 */
@Repository
public interface ContractToOperationRowRepository extends CrudRepository<ContractToOperationRow, Long> {

    Optional<ContractToOperationRow> findByContractId(Long id);
    Optional<ContractToOperationRow> findByOperationRowId(Long id);

}
