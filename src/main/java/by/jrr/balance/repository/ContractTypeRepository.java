package by.jrr.balance.repository;


import by.jrr.balance.bean.ContractType;
import by.jrr.balance.bean.OperationCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ideapad on 27.9.17.
 */

public interface ContractTypeRepository extends CrudRepository<ContractType, Long> {

}
