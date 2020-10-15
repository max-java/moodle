package by.jrr.balance.repository;


import by.jrr.balance.bean.OperationCategory;
import by.jrr.balance.constant.OperationRowDirection;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ideapad on 27.9.17.
 */

public interface OperationCategoryRepository extends CrudRepository<OperationCategory, Long> {
    List<OperationCategory> findByOperationRowDirection(OperationRowDirection operationRowDirection);


}
