package by.jrr.balance.repository;


import by.jrr.balance.bean.AcceptanceAct;
import by.jrr.balance.bean.Contract;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by ideapad on 27.9.17.
 */

@Repository
public interface AcceptanceActRepository extends CrudRepository<AcceptanceAct, Long> {

//    Contract findFirstByOrderByDateAsc();
//    List<Contract> findAllByIdIn(List<Long> ids);
    Optional<AcceptanceAct> findByContractId(Long id);

}
