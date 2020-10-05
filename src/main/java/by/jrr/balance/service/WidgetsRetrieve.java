package by.jrr.balance.service;

import by.jrr.balance.bean.Currency;
import by.jrr.balance.bean.Goal;
import by.jrr.balance.bean.OperationRow;
import by.jrr.balance.beanrepository.OperationRowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shelkovich Maksim on 20.6.19.
 *
 * This class is need to accumulate widget values form services
 *
 */
@Service
public class WidgetsRetrieve {

    @Autowired
    CashFlowService cashFlowService;

    @Autowired
    GoalService goalService;

    @Autowired
    OperationRowRepository operationRowRepository;

    public List<Pair<Double, Currency>> getMinimalTotals() {
        return cashFlowService.getMinimalTotals();
    }

    public List<Goal> getAchievedGoals() { // TODO: 20.6.19 widget displays only first goal. delete unused to increase performance
        return goalService.getAchievedAndNegativeGoals().get("achievedGoals");
    }
    public List<Goal> getNegativeGoals() { // TODO: 20.6.19 widget displays only first goal. delete unused to increase performance
        return goalService.getAchievedAndNegativeGoals().get("negativeGoals");
    }

    public List<OperationRow> getPlanOutcomesForToday() {
        List<OperationRow> outcomesForToday = new ArrayList<>();
        try{
            LocalDate toDate = LocalDate.now();
            LocalDate fromDate = operationRowRepository.findFirstByOrderByDateAsc().getDate();
//            outcomesForToday = operationRowRepository.findAllByIdCashFlowDirectionAndDateBetween(OperationRowDirection.PLAN_OUTCOME, fromDate, toDate);
        } catch (Exception ex) {
        }
        return outcomesForToday;
    }




}
