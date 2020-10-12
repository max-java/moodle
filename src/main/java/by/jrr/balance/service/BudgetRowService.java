package by.jrr.balance.service;

import by.jrr.balance.bean.Currency;
import by.jrr.balance.bean.OperationCategory;
import by.jrr.balance.bean.OperationRow;

import by.jrr.balance.repository.OperationCategoryRepository;
import by.jrr.balance.repository.OperationRowRepository;
import by.jrr.balance.beantransient.BudgetRow;
import by.jrr.balance.beantransient.BudgetTotal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Shelkovich Maksim on 13.6.19.
 *
 * This class is need to render budget table on budget page
 */
@Service
public class BudgetRowService {

    @Autowired
    OperationRowRepository operationRowRepository;
    @Autowired
    OperationCategoryRepository operationCategoryRepository;

    private List<OperationRow> operationRowsSelectedMonthsIncome;
    private List<OperationRow> operationRowsPreviousMonth; //outcome
    private List<OperationRow> operationRowsSelectedMonth; //outcome
    private List<OperationRow> operationRowsSelectedMonthBudget;
    private List<OperationRow> operationRowsSelectedMonthFactOutcomes;
    private List<OperationRow> operationRowsSelectedMonthPlanOutcomes;
    private List<OperationCategory> categories;
    private List<Currency> currencies;

    Map<Pair<String, String>, Double> previousMonthTotals = new HashMap<>();
    Map<Pair<String, String>, Double> selectedMonthTotals = new HashMap<>(); // TODO: 6/17/19 use it in budget table ?
    Map<Pair<String, String>, Double> selectedMonthBudget = new HashMap<>();
    Map<Pair<String, String>, Double> selectedMonthFactOutcomes = new HashMap<>();
    Map<Pair<String, String>, Double> selectedMonthPlanOutcomes = new HashMap<>();

    // TODO: 13.6.19 make it with one call to database
    public List<BudgetRow> getBudgetForMonth(LocalDate dateOfMonth) {
        this.setFieldsFromDB(dateOfMonth);
        this.calculateTotalsForOperationsAndSet();
        List<BudgetRow> budgetRows = this.createBudgetRowListAndSetOperationCategories();
        budgetRows = this.setPreviousMonthTotals(budgetRows);
        budgetRows = this.setSelectedMonthTotals(budgetRows);
        budgetRows = this.setSelectedMonthBudget(budgetRows);
        budgetRows = this.setSelectedMonthFactOutcomes(budgetRows);
        budgetRows = this.setSelectedMonthPlanOutcomes(budgetRows);

        //filter empty rows
        budgetRows = budgetRows.stream().filter(budgetRow ->
                                            budgetRow.getPreviousMonthTotal() != 0
                                            || budgetRow.getSelectedMonthTotal() != 0
                                            || budgetRow.getSelectedMonthBudget() != 0
                                            || budgetRow.getSelectedMonthFactOutcomes() != 0
                                            || budgetRow.getSelectedMonthPlanOutcomes() != 0)
                                            .collect(Collectors.toList());

        return budgetRows;

    }
    public List<BudgetTotal> getBudgetTotalsForMonthAndCurrency(LocalDate dateOfMonth) {
        this.setFieldsFromDB(dateOfMonth);
        List<BudgetTotal> budgetTotalList = new ArrayList<>();
        for (Currency currency : currencies) {
            BudgetTotal budgetTotal = new BudgetTotal();
            budgetTotal.setCurrency(currency);

//            budgetTotal.setTotalIncomesForMonth(operationRowsSelectedMonthsIncome.stream()
//                    .filter(operationRow -> operationRow.getCurrency().getId() == currency.getId())
//                    .collect(Collectors.summingDouble(operationRow -> operationRow.getSum())));
//
//            budgetTotal.setTotalOutcomesForMonth(operationRowsSelectedMonth .stream()
//                    .filter(operationRow -> operationRow.getCurrency().getId() == currency.getId())
//                    .collect(Collectors.summingDouble(operationRow -> operationRow.getSum())));
//
//            budgetTotal.setTotalBudgetForMonth(operationRowsSelectedMonthBudget.stream()
//                    .filter(operationRow -> operationRow.getCurrency().getId() == currency.getId())
//                    .collect(Collectors.summingDouble(operationRow -> operationRow.getSum())));


            budgetTotalList.add(budgetTotal);
        }
        return budgetTotalList;
    }
    public List<OperationRow> getBudgetOperationRowsForSelectedMonth(LocalDate localDate) {
        this.setFieldsFromDB(localDate); // TODO: 6/17/19 make it one call to database (it could return Map)
        return this.operationRowsSelectedMonthBudget;
    }



    private List<BudgetRow> createBudgetRowListAndSetOperationCategories() {
        List<BudgetRow> budgetRowList = new ArrayList<>();
        for (OperationCategory category : categories) {
            for (Currency currency : currencies) {
                BudgetRow budgetRow = new BudgetRow();
                budgetRow.setOperationCategory(category);
                budgetRow.setCurrency(currency);
                budgetRowList.add(budgetRow);
            }
        }
        return budgetRowList;
    }

    private List<BudgetRow> setPreviousMonthTotals(List<BudgetRow> budgetRows) {
        budgetRows.stream().forEach(budgetRow -> budgetRow.setPreviousMonthTotal(
                this.previousMonthTotals.getOrDefault(
                        Pair.of(
                                budgetRow.getOperationCategory().getName(),
                                budgetRow.getCurrency().getName()
                        ),
                        0.00)
                )
        );
        return budgetRows;
    }

    private List<BudgetRow> setSelectedMonthTotals(List<BudgetRow> budgetRows) {
        budgetRows.stream().forEach(budgetRow -> budgetRow.setSelectedMonthTotal(
                this.selectedMonthTotals.getOrDefault(
                        Pair.of(
                            budgetRow.getOperationCategory().getName(),
                            budgetRow.getCurrency().getName()
                        ),
                        0.00)
                )
        );
        return budgetRows;
    }

    private List<BudgetRow> setSelectedMonthBudget(List<BudgetRow> budgetRows) {
        budgetRows.stream().forEach(budgetRow -> budgetRow.setSelectedMonthBudget(
                this.selectedMonthBudget.getOrDefault(
                        Pair.of(
                                budgetRow.getOperationCategory().getName(),
                                budgetRow.getCurrency().getName()
                        ),
                        0.00)
                )
        );
        // budget is an operation row with operationRowDirection=BUDGET, with could be only one operation. This use to add, delete and update MonthBudgetValue as operationRow
        return budgetRows;
    }
    private List<BudgetRow> setSelectedMonthFactOutcomes(List<BudgetRow> budgetRows) {
        budgetRows.stream().forEach(budgetRow -> budgetRow.setSelectedMonthFactOutcomes(
                this.selectedMonthFactOutcomes.getOrDefault(
                        Pair.of(
                                budgetRow.getOperationCategory().getName(),
                                budgetRow.getCurrency().getName()
                        ),
                        0.00)
                )
        );
        return budgetRows;
    }
    private List<BudgetRow> setSelectedMonthPlanOutcomes(List<BudgetRow> budgetRows) {
        budgetRows.stream().forEach(budgetRow -> budgetRow.setSelectedMonthPlanOutcomes(
                this.selectedMonthPlanOutcomes.getOrDefault(
                        Pair.of(
                                budgetRow.getOperationCategory().getName(),
                                budgetRow.getCurrency().getName()
                        ),
                        0.00)
                )
        );
        return budgetRows;
    }


    private void setFieldsFromDB(LocalDate dateOfMonth) {
        LocalDate previousMonth = dateOfMonth.minusMonths(1);
        LocalDate dateFrom = previousMonth.withDayOfMonth(1);
        LocalDate dateTo = dateOfMonth.withDayOfMonth(dateOfMonth.lengthOfMonth());
//
//        List<OperationRow> outcomeList = operationRowRepository
//                .findAllByIdCashFlowDirectionAndDateBetween(OperationRowDirection.OUTCOME, dateFrom, dateTo);
//        List<OperationRow> planOutcomeList = operationRowRepository
//                .findAllByIdCashFlowDirectionAndDateBetween(OperationRowDirection.PLAN_OUTCOME, dateFrom, dateTo);
//
//        List<OperationRow> incomeList = operationRowRepository
//                .findAllByIdCashFlowDirectionAndDateBetween(OperationRowDirection.INCOME, dateFrom, dateTo);
//        List<OperationRow> planIncomeList = operationRowRepository
//                .findAllByIdCashFlowDirectionAndDateBetween(OperationRowDirection.PLAN_INCOME, dateFrom, dateTo);
//
//        List<OperationRow> budgetListList = operationRowRepository
//                .findAllByIdCashFlowDirectionAndDateBetween(OperationRowDirection.BUDGET, dateOfMonth.withDayOfMonth(1), dateTo);

        List<OperationRow> allList = new ArrayList<>();
//        allList.addAll(outcomeList);
//        allList.addAll(planOutcomeList);

        List<OperationRow> allIncome = new ArrayList<>();
//        allIncome.addAll(incomeList);
//        allIncome.addAll(planIncomeList);

        this.operationRowsSelectedMonth = allList.stream()
                .filter(operationRow -> operationRow.getDate().isAfter(dateOfMonth.withDayOfMonth(1).minusDays(1)))
                .collect(Collectors.toList());
        this.operationRowsPreviousMonth = allList.stream()
                .filter(operationRow -> operationRow.getDate().isBefore(dateOfMonth.withDayOfMonth(1)))
                .collect(Collectors.toList());
        this.operationRowsSelectedMonthsIncome = allIncome.stream()
                .filter(operationRow -> operationRow.getDate().isAfter(dateOfMonth.withDayOfMonth(1).minusDays(1)))
                .collect(Collectors.toList());
//        this.operationRowsSelectedMonthBudget = budgetListList;
//        this.operationRowsSelectedMonthFactOutcomes = outcomeList.stream()
//                .filter(operationRow -> operationRow.getDate().isAfter(dateOfMonth.withDayOfMonth(1).minusDays(1)))
//                .collect(Collectors.toList());
//        this.operationRowsSelectedMonthPlanOutcomes = planOutcomeList.stream()
//                .filter(operationRow -> operationRow.getDate().isAfter(dateOfMonth.withDayOfMonth(1).minusDays(1)))
//                .collect(Collectors.toList());

//        this.categories = operationCategoryRepository.findByIdCashFlowDirection(OperationRowDirection.OUTCOME);
//        this.currencies = (List) currencyRepository.findAll();



        // TODO: 6/16/19 simplify, avoid request to database because I have all I need
        for (OperationRow operationRow : operationRowsSelectedMonthsIncome) {
//            operationRow.setCurrency(currencyRepository.findById(operationRow.getIdCurrency()).get());
//            operationRow.setOperationCategory(operationCategoryRepository.findById(operationRow.getIdOperationCategory()).get());
        }
        for (OperationRow operationRow : operationRowsSelectedMonth) {
//            operationRow.setCurrency(currencyRepository.findById(operationRow.getIdCurrency()).get());
//            operationRow.setOperationCategory(operationCategoryRepository.findById(operationRow.getIdOperationCategory()).get());
        }
        for (OperationRow operationRow : operationRowsPreviousMonth) {
//            operationRow.setCurrency(currencyRepository.findById(operationRow.getIdCurrency()).get());
//            operationRow.setOperationCategory(operationCategoryRepository.findById(operationRow.getIdOperationCategory()).get());
        }
        for (OperationRow operationRow : operationRowsSelectedMonthBudget) {
//            operationRow.setCurrency(currencyRepository.findById(operationRow.getIdCurrency()).get());
//            operationRow.setOperationCategory(operationCategoryRepository.findById(operationRow.getIdOperationCategory()).get());
        }

    }

    private void calculateTotalsForOperationsAndSet() {
//        this.previousMonthTotals = calculateTotalsForOperations(this.operationRowsPreviousMonth);
//        this.selectedMonthTotals = calculateTotalsForOperations(this.operationRowsSelectedMonth);
//        this.selectedMonthBudget = calculateTotalsForOperations(this.operationRowsSelectedMonthBudget);
//        this.selectedMonthFactOutcomes = calculateTotalsForOperations(this.operationRowsSelectedMonthFactOutcomes);
//        this.selectedMonthPlanOutcomes = calculateTotalsForOperations(this.operationRowsSelectedMonthPlanOutcomes);
    }

//    private Map<Pair<String, String>, Double> calculateTotalsForOperations(List<OperationRow> operations) {
//
//        return operations.stream()
//                .collect(Collectors
//                        .groupingBy(
//                                operation -> (
//                                        Pair.of(
//                                                operation.getOperationCategoryName(),
//                                                operation.getCurrencyName()
//                                        )
//                                ),
//                                Collectors.summingDouble(operation -> operation.getSum())
//                        )
//                );
//    }
}

