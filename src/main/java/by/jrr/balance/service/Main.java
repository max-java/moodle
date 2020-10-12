package by.jrr.balance.service;

import by.jrr.balance.bean.*;
import by.jrr.balance.repository.*;
import by.jrr.balance.beantransient.Balance;
import by.jrr.balance.constant.OperationRowDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shelkovich Maksim on 23.2.18.
 */
@Service
public class Main {

    @Autowired
    TotalRepository totalRepository;

    @Autowired
    OperationRowRepository operationRowRepository;
    @Autowired
    OperationCategoryRepository operationCategoryRepository;

    @Autowired
    OperationRowService operationRowService;
    @Autowired
    CashFlowService cashFlowService;

    @Autowired
    OperationToProfileService operationToProfileService;

    // TODO: 13.6.19 Do I really need duplication for PLAN_ ?
    public void actionAddOperationCategory(String name, int idCashFlowDirection) {
//        if (idCashFlowDirection==OperationRowDirection.INCOME || idCashFlowDirection==OperationRowDirection.PLAN_INCOME) {
//            OperationCategory oc1 = new OperationCategory(name, OperationRowDirection.INCOME);
//            operationCategoryRepository.save(oc1);
//        }
//        else if (idCashFlowDirection==OperationRowDirection.OUTCOME || idCashFlowDirection==OperationRowDirection.PLAN_OUTCOME) {
//            OperationCategory oc1 = new OperationCategory(name, OperationRowDirection.OUTCOME);
//            operationCategoryRepository.save(oc1);
//        } else {
//            OperationCategory oc = new OperationCategory(name, OperationRowDirection.UNKNOWN);
//            operationCategoryRepository.save(oc);
//        }
    }

    public List<OperationCategory> getOperationCategoryByCashFlowDirection(int idCashFlowDirection) {
        return (List<OperationCategory>) operationCategoryRepository.findByIdCashFlowDirection(idCashFlowDirection);
    }

    /** unlimited */
    public List<OperationRow> getAllOperationRowsByCashFlowDirectionOrderByDateDesc(OperationRowDirection direction) {
        List<OperationRow> incomeRowList = operationRowRepository.findAllByOperationRowDirectionOrderByDateDesc(direction);
        setCategory(incomeRowList); // TODO: 07/03/20 it is not income list, it is operation rows list
        setCurrency(incomeRowList);
        return incomeRowList;
    }
    /** unlimited */
    public List<OperationRow> getAllOperationRowsByCashFlowDirectionOrderByDateAsc(OperationRowDirection direction) {
        List<OperationRow> incomeRowList = operationRowRepository.findAllByOperationRowDirectionOrderByDateAsc(direction);
        setCategory(incomeRowList); // TODO: 07/03/20 it is not income list, it is operation rows list
        setCurrency(incomeRowList);
        return incomeRowList;
    }

    /** limited by first 100*/
    public List<OperationRow> getFirstHundredOperationRowsByCashFlowDirectionOrderByDateDesc(OperationRowDirection direction) {
        List<OperationRow> incomeRowList = operationRowRepository.findFirst100ByOperationRowDirectionOrderByDateDesc(direction);
        setCategory(incomeRowList); // TODO: 07/03/20 it is not income list, it is operation rows list
        setCurrency(incomeRowList);
        return incomeRowList;
    }
    /** limited by first 100*/
    public List<OperationRow> getFirstHundredOperationRowsByCashFlowDirectionOrderByDateAsc(OperationRowDirection direction) {
        List<OperationRow> incomeRowList = operationRowRepository.findFirst100ByOperationRowDirectionOrderByDateAsc(direction);
        setCategory(incomeRowList); // TODO: 07/03/20 it is not income list, it is operation rows list
        setCurrency(incomeRowList);
        return incomeRowList;
    }

    public void processOperationRowForm(Long profileId,
                                        Long subscriberId,
                                        Long id,
                                        OperationRowDirection operationRowDirection,
                                        LocalDate date,
                                        Double sum,
                                        Currency currency,
                                        Long idOperationCategory,
                                        String note,
                                        Integer repeatNTimes,
                                        String repeatingFrequency,
                                        String endOfRepeatingDate,
                                        Long contractId,
                                        String action) {
        if (action.equals("save")) {
            List<OperationRow> result = operationRowService.actionEditOperationRow(id,
                    operationRowDirection,
                    date,
                    sum,
                    currency,
                    idOperationCategory,
                    note,
                    repeatNTimes,
                    repeatingFrequency,
                    endOfRepeatingDate);

            if(result != null && result.size() > 0) { // TODO: 12/10/2020 simplify and split
                if(subscriberId != null || profileId != null || contractId != null) {
                    Long rowId = result.get(0).getId();
                    OperationToProfile operationToProfile = OperationToProfile.builder()
                            .id(operationToProfileService.getOperationToProfileIdByOperationRoleId(rowId))
                            .operationRowId(rowId)
                            .subscriberId(subscriberId)
                            .streamId(profileId)
                            .contractId(contractId)
                            .build();

                    operationToProfileService.save(operationToProfile);
                }

            }
        } else if (action.equals("delete")) {
            operationRowService.deleteRow(id);
        } else if (action.equals("move")) {
            operationRowService.moveRowFromPlanToFact(id,
                    date,
                    sum,
                    currency,
                    note);
        } else if (action.equals("moveToGoals")) {
            operationRowService.moveRowFromPlanToGoals(id);
        }
    }


    // TODO: 4.3.18 work with balance move to another class
    //get all balances for all currencies
    public List<Balance> getBalance() {
//        List<Currency> currencies = (List)currencyRepository.findAll();
        List<Balance> balanceList = new ArrayList<>();
//        for (Currency currency : currencies) {
////            balanceList.add(getBalance(currency.getId()));
//        }
        return balanceList;
    }
    // get all Plan balance for all currencies
    public List<Balance> getPlanBalance() {
//        List<Currency> currencies = (List)currencyRepository.findAll();
        List<Balance> balanceList = new ArrayList<>();
//        for (Currency currency : currencies) {
////            balanceList.add(getPlanBalance(currency.getId()));
//        }
        return balanceList;
    }
    //get balance for specific currency
    public Balance getBalance(long idCurrency) {
        double sumIncome=0;
        double sumOutcome=0;
//        List<OperationRow> operationRowList = operationRowRepository.findAllByIdCurrencyOrderByDateAsc(idCurrency);
//        for(OperationRow operationRow : operationRowList) {
////            if (operationRow.getIdCashFlowDirection()==OperationRowDirection.INCOME) {
////                sumIncome = sumIncome+operationRow.getSum();
////            }
////            if (operationRow.getIdCashFlowDirection()==OperationRowDirection.OUTCOME) {
////                sumOutcome = sumOutcome+ operationRow.getSum();
////            }
//        }
        Balance balance = new Balance();
//        balance.setCurrency(currencyRepository.findById(idCurrency).get());
        balance.setTotal(sumIncome-sumOutcome);
        // TODO: 8.4.18 round total value !
        balance.setTotal(Math.round(balance.getTotal()));
        return balance;
    }
    //get plan balance for specific currency
    public Balance getPlanBalance(Long idCurrency) { // TODO: 4.3.18 make it simple and multifunctional
        double sumPlanIncome = 0;
        double sumPlanOutcome = 0;
//        List<OperationRow> operationRowList = operationRowRepository.findAllByIdCurrencyOrderByDateAsc(idCurrency);
//        for (OperationRow operationRow : operationRowList) {
////            if (operationRow.getIdCashFlowDirection()==OperationRowDirection.PLAN_INCOME) {
////                sumPlanIncome = sumPlanIncome + operationRow.getSum();
////            }
////            if (operationRow.getIdCashFlowDirection()==OperationRowDirection.PLAN_OUTCOME) {
////                sumPlanOutcome = sumPlanOutcome + operationRow.getSum();
////            }
//        }
        Balance balance = new Balance();
//        balance.setCurrency(currencyRepository.findById(idCurrency).get());
        balance.setTotal(getBalance(idCurrency).getTotal()-sumPlanOutcome+sumPlanIncome);
        // TODO: 8.4.18 round total value !
        balance.setTotal(Math.round(balance.getTotal()));
        return balance;
    }
/////// END OF BALANCES
    private void setCategory(List<OperationRow> operationRowList){
        for (OperationRow operationRow : operationRowList) {
//            operationRow.setOperationCategory(operationCategoryRepository.findById(operationRow.getIdOperationCategory()).get());
        }
    }
    private void setCurrency(List<OperationRow> operationRowList){
        for (OperationRow operationRow : operationRowList) {
//            operationRow.setCurrency(currencyRepository.findById(operationRow.getIdCurrency()).get());
        }
    }

    //load list of currencies from database
//    public List<Currency> getCurrencies() {
//        return (List) currencyRepository.findAll();
//    }
    //SAVE NEW CURRENCY

// end of totals
    public void deleteOoperationRow(Long idOperationRow) {
        operationRowRepository.deleteById(idOperationRow);
    }
}
