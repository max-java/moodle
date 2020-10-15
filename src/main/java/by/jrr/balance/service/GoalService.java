package by.jrr.balance.service;

import by.jrr.balance.bean.Goal;
import by.jrr.balance.bean.OperationRow;
import by.jrr.balance.repository.GoalRepository;
import by.jrr.balance.repository.OperationCategoryRepository;
import by.jrr.balance.repository.OperationRowRepository;
import by.jrr.balance.beantransient.CashFlowRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shelkovich Maksim on 10.6.19.
 */
@Service
public class GoalService {

    // TODO: 6/18/19 split crud operations and prediction

    @Autowired
    GoalRepository goalRepository;
    @Autowired
    OperationCategoryRepository operationCategoryRepository;

    @Autowired
    CashFlowService cashFlowService;
    @Autowired
    OperationRowService operationRowService;
    @Autowired
    OperationRowRepository operationRowRepository; // TODO: 6/18/19 replace by crud service

    public List<Goal> findAll() {
        List<Goal> goalList = (List) goalRepository.findAll();
        setCategory(goalList);
        setCurrency(goalList);
        return goalList;
    }

    public Goal saveGoal(Goal goal) {
        return goalRepository.save(goal);
    }

    public void createFromFormAndSaveGoal(
            Long idGoal,
            double sum,
            Long currencyId,
            Long idOperationCategory,
            String note,
            int priority,
            String submit
    ) {
        Goal goal = new Goal();
        if (idGoal != null) {
            goal = goalRepository.findById(idGoal).get();
        }
        goal.setSum(sum);
        goal.setIdCurrency(currencyId);
        goal.setIdOperationCategory(idOperationCategory);
        goal.setNote(note);
        goal.setPriority(priority);
        saveGoal(goal);
    }

    public void moveGoalToPlanOutcomes(
            Long idGoal,
            double sum,
            Long currencyId,
            Long idOperationCategory,
            String note,
            int priority,
            String submit,
            String date
    ) {
        Goal goal = new Goal();
        if (idGoal != null) {
            goal = goalRepository.findById(idGoal).get();
        }
        goal.setSum(sum);
        goal.setIdCurrency(currencyId);
        goal.setIdOperationCategory(idOperationCategory);
        goal.setNote(note);
        goal.setPriority(priority);
        goal.setDate(LocalDate.parse(date));

        OperationRow operationRow = new OperationRow();
        operationRow.setDate(goal.getDate());
        operationRow.setSum(goal.getSum());
//        operationRow.setIdOperationCategory(goal.getIdOperationCategory()); // TODO: 6/18/19 is it sets?
//        operationRow.setIdCashFlowDirection(OperationRowDirection.PLAN_OUTCOME);
        operationRow.setNote(goal.getNote());
//        operationRow.setIdCurrency(goal.getIdCurrency());
        operationRowRepository.save(operationRow);
        this.deleteGoal(goal.getId());

    }

    public void moveNegativeGoalToGoals(Long idNegativeGoal) {
        //idNegativeGoal is a negative operationId
        //take outcomeRow by ID from database and save it as goal.
        Goal goal = new Goal();
        long idOperationRow = idNegativeGoal * -1;
        OperationRow operationRow = operationRowRepository.findById(idOperationRow).get();
        goal.setSum(operationRow.getSum());
//        goal.setIdCurrency(operationRow.getIdCurrency());
//        goal.setIdOperationCategory(operationRow.getIdOperationCategory());
        goal.setNote(operationRow.getNote());
        goal.setPriority(0);
        saveGoal(goal);
        operationRowRepository.deleteById(idOperationRow);
    }

    public void moveOperationRowToGoals(Long idOperationRow) {
        //take outcomeRow by ID from database and save it as goal.
        Goal goal = new Goal();
        OperationRow operationRow = operationRowRepository.findById(idOperationRow).get();
        goal.setSum(operationRow.getSum());
//        goal.setIdCurrency(operationRow.getIdCurrency());
//        goal.setIdOperationCategory(operationRow.getIdOperationCategory());
        goal.setNote(operationRow.getNote());
        goal.setPriority(0);
        saveGoal(goal);
        operationRowRepository.deleteById(idOperationRow);
    }


    public void deleteGoal(Goal goal) {
        goalRepository.deleteById(goal.getId());
    }

    public void deleteGoal(Long idGoal) {
        goalRepository.deleteById(idGoal);
    }

    public Map<String, List<Goal>> getAchievedAndNegativeGoals() {
        Map<String, List<Goal>> result = new HashMap<>();
        List<Goal> allAchievedGoals = new ArrayList<>();
        List<Goal> allNegativeGoals = new ArrayList<>();
        List<Goal> goalList = this.findAll();
//        List<Long> currenciesIdList = goalList.stream().map(goal -> goal.getIdCurrency()).distinct().collect(Collectors.toList()); //get distinct list of currencies todo not sure that it should be taken from DB
//        List<Currency> currencyList = (List) currencyRepository.findAll();
//        List<Long> currenciesIdList = currencyList.stream().map(currency -> currency.getId()).distinct().collect(Collectors.toList()); //get distinct list of currencies


//        for (Long currId : currenciesIdList) {
//            List<Goal> achievedGoals = new ArrayList<>();
//            List<Goal> negativeGoals = new ArrayList<>();
//            List<CashFlowRow> cashFlow = cashFlowService.getCashFlow(currId);
//            List<Goal> goalListForCurrency = goalList.stream().filter(goal -> goal.getIdCurrency() == currId).collect(Collectors.toList());
//
//            for (CashFlowRow cashFlowRow : cashFlow) {
//                if (cashFlowRow.getTotal() < 0 ) {
//                    addToNegative(negativeGoals, cashFlowRow);
//                }
//                while (!this.isSumOfAchievedGoalsLessThanTotal(cashFlowRow, achievedGoals) && achievedGoals.size() > 0) {
//                    this.moveFromAchievedToGoals(achievedGoals, goalListForCurrency);
//                }
//
//                if (goalListForCurrency.size() > 0) { // if there are goals for current currency
//                    while (goalListForCurrency.size() > 0 && isSumOfAchievedGoalAndNextOneLessThanTotal(achievedGoals, goalListForCurrency, cashFlowRow)) { //while there are non-achieved goals and new goal van be added
//                        this.moveFromGoalsToAchievedAndSetDate(goalListForCurrency, achievedGoals, cashFlowRow.getDate());
//                    }
//                }
//            }
//            allAchievedGoals.addAll(achievedGoals);
//            allNegativeGoals.addAll(negativeGoals);
//        }
        setCategory(allNegativeGoals);
        setCurrency(allNegativeGoals);
        result.put("achievedGoals", allAchievedGoals);
        result.put("negativeGoals", allNegativeGoals);
        return result;
    }
    // TODO: 6/17/19 remove duplicated code

    private void setCategory(List<Goal> goalList) {
        for (Goal goal : goalList) {
            goal.setOperationCategory(operationCategoryRepository.findById(goal.getIdOperationCategory()).get());
        }
    }

    private void setCurrency(List<Goal> goalList) {
        for (Goal goal : goalList) {
//            goal.setCurrency(currencyRepository.findById(goal.getIdCurrency()).get());
        }
    }

    private boolean isSumOfAchievedGoalsLessThanTotal(CashFlowRow cashFlowRow, List<Goal> achievedGoals) {
        BigDecimal sumOfAchieved = achievedGoals.stream()
                .map(Goal::getSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return (sumOfAchieved.compareTo(BigDecimal.valueOf(cashFlowRow.getTotal())) <= 0);

    }

    private boolean isSumOfAchievedGoalAndNextOneLessThanTotal(List<Goal> achievedGoals, List<Goal> goalListForCurrency, CashFlowRow cashFlowRow) {
        //select goal for cashFlow currency
        goalListForCurrency.sort(Goal::compareByPriority);
        Goal nextGoal = goalListForCurrency.get(0);
        BigDecimal sumOfAchievedAndNextOne = nextGoal.getSum().add(
                achievedGoals.stream()
                        .map(Goal::getSum)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));

        return sumOfAchievedAndNextOne.compareTo(BigDecimal.valueOf(cashFlowRow.getTotal())) <= 0;
    }

    private void moveFromGoalsToAchievedAndSetDate(List<Goal> goalListForCurrency, List<Goal> achievedGoals, LocalDate dateAchieved) {
        Goal achievedGoal = goalListForCurrency.get(0);
        Goal goal = new Goal();
        goal.setCurrency(achievedGoal.getCurrency());
        goal.setIdCurrency(achievedGoal.getIdCurrency());
        goal.setDate(dateAchieved);
        goal.setSum(achievedGoal.getSum());
        goal.setNote(achievedGoal.getNote());
        goal.setOperationCategory(achievedGoal.getOperationCategory());
        goal.setIdOperationCategory(achievedGoal.getIdOperationCategory());
        goal.setId(achievedGoal.getId());
        goal.setPriority(achievedGoal.getPriority());
        achievedGoals.add(goal);
        goalListForCurrency.remove(0);

    }

    private void moveFromAchievedToGoals(List<Goal> achievedGoals, List<Goal> goalListForCurrency) {
        if (achievedGoals.size() > 0) {
            int deleteIndex = achievedGoals.size() - 1;
            goalListForCurrency.add(achievedGoals.get(deleteIndex));
            achievedGoals.remove(deleteIndex);
        }
    }

    private void addToNegative(List<Goal> negativeGoals, CashFlowRow cashFlowRow) {
        Goal goal = new Goal();
        goal.setCurrency(cashFlowRow.getOperationRow().getCurrency());
//        goal.setIdCurrency(cashFlowRow.getOperationRow().getIdCurrency());
        goal.setDate(cashFlowRow.getDate());
        goal.setSum(cashFlowRow.getTotal());
        goal.setNote(cashFlowRow.getNote());
//        goal.setOperationCategory(cashFlowRow.getOperationRow().getOperationCategory());
//        goal.setIdOperationCategory(cashFlowRow.getOperationRow().getIdOperationCategory());
        long id = cashFlowRow.getOperationRow().getId() * -1L;
        goal.setId(id); //set id the same as operation so i can delete operation by Id from negative goal when move it to goals, but set Id to negative to avoid duplication with achieved goals.
        negativeGoals.add(goal);
    }
}
