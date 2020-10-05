package by.jrr.balance.beanrepository;


import by.jrr.balance.bean.OperationRow;
import by.jrr.balance.constant.OperationRowDirection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by ideapad on 27.9.17.
 */

@Repository
public interface OperationRowRepository extends CrudRepository<OperationRow, Long> {

    OperationRow findFirstByOrderByDateAsc();
    List<OperationRow> findAllByIdIn(List<Long> ids);

//    List<OperationRow> findAllByIdCurrencyOrderByDateAsc(Long idCurrency);

    List<OperationRow> findAllByOperationRowDirectionOrderByDateDesc(OperationRowDirection direction);
    List<OperationRow> findAllByOperationRowDirectionOrderByDateAsc(OperationRowDirection direction);

    List<OperationRow> findFirst100ByOperationRowDirectionOrderByDateDesc(OperationRowDirection direction);
    List<OperationRow> findFirst100ByOperationRowDirectionOrderByDateAsc(OperationRowDirection direction);
    List<OperationRow> findAllByRepeatableToken(Integer token);
}
