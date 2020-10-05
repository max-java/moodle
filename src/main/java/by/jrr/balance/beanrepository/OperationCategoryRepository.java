package by.jrr.balance.beanrepository;


import by.jrr.balance.bean.OperationCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ideapad on 27.9.17.
 */

public interface OperationCategoryRepository extends CrudRepository<OperationCategory, Long> {
    List<OperationCategory> findByIdCashFlowDirection(int id);


}
