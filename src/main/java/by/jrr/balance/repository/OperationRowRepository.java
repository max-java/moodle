package by.jrr.balance.repository;


import by.jrr.balance.bean.OperationRow;
import by.jrr.balance.constant.OperationRowDirection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ideapad on 27.9.17.
 */

@Repository
public interface OperationRowRepository extends CrudRepository<OperationRow, Long> {

    OperationRow findFirstByOrderByDateAsc();
    List<OperationRow> findAllByIdIn(List<Long> ids);
    //todo add findInBetween dates and before date and after date?

//    List<OperationRow> findAllByIdCurrencyOrderByDateAsc(Long idCurrency);

    List<OperationRow> findAllByOperationRowDirectionOrderByDateDesc(OperationRowDirection direction);
    List<OperationRow> findAllByOperationRowDirectionOrderByDateAsc(OperationRowDirection direction);

    List<OperationRow> findFirst100ByOperationRowDirectionOrderByDateDesc(OperationRowDirection direction);
    List<OperationRow> findFirst100ByOperationRowDirectionOrderByDateAsc(OperationRowDirection direction);
    List<OperationRow> findAllByRepeatableToken(Integer token);
}
