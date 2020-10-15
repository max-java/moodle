package by.jrr.balance.repository;


import by.jrr.balance.bean.Contract;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ideapad on 27.9.17.
 */

@Repository
public interface ContractRepository extends CrudRepository<Contract, Long> {

    Contract findFirstByOrderByDateAsc();
    List<Contract> findAllByIdIn(List<Long> ids);

}
