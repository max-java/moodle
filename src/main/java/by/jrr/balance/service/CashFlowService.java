package by.jrr.balance.service;

import by.jrr.balance.bean.Currency;
import by.jrr.balance.beanrepository.OperationRowRepository;
import by.jrr.balance.beantransient.CashFlowRow;
import by.jrr.balance.beantransient.CashFlowWithCurrency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by Shelkovich Maksim on 11.6.19.
 * <p>
 * creates cash flow and based on total sets goal date. If total negative - adds to goal.
 */
@Service
public class CashFlowService {

    // TODO: 12.6.19 split by currencies on the highest level possible

    @Autowired
    OperationRowRepository operationRowRepository;

    public List<CashFlowWithCurrency> getCashFlow() {

        List<CashFlowWithCurrency> cashFlowWithCurrencies = new ArrayList<>();
//        List<Currency> currencies = (List) currencyRepository.findAll();
//        for (Currency currency : currencies) {
//            CashFlowWithCurrency cashFlowWithCurrency = new CashFlowWithCurrency();
////            cashFlowWithCurrency.setCashFlowRows(getCashFlow(currency.getId()));
//            cashFlowWithCurrency.setCurrency(currency);
//            cashFlowWithCurrencies.add(cashFlowWithCurrency);
//        }
        return cashFlowWithCurrencies;
    }

    public List<Pair<Double, Currency>> getMinimalTotals() {
        List<CashFlowWithCurrency> cashFlowList = this.getCashFlow();
        List<Pair<Double, Currency>> minimalTotals = new ArrayList<>();
        Map<Currency, LocalDate> filterDates = new HashMap<>();

        // to filter rows that in the past I need to use date.now or date of planned operation - witch will be earlier.
        for (CashFlowWithCurrency cashFlowWithCurrency : cashFlowList) {
            LocalDate filterDate = LocalDate.now();
            Optional<LocalDate> minDate = cashFlowWithCurrency.getCashFlowRows().stream()
//                    .filter(cashFlowRow -> cashFlowRow.getOperationRow().getIdCashFlowDirection() == OperationRowDirection.PLAN_OUTCOME)
                    .map(cashFlowRow -> cashFlowRow.getDate())
                    .min(LocalDate::compareTo);
            LocalDate resultDate = minDate.orElse(LocalDate.now());
            if(resultDate.isBefore(filterDate)) {
                filterDate = resultDate;
            }
            filterDates.put(cashFlowWithCurrency.getCurrency(), filterDate);
        }


        for (CashFlowWithCurrency cashFlowWithCurrency : cashFlowList) {
            Double minTotal = cashFlowWithCurrency.getCashFlowRows().stream()
                    .filter(cashFlowRow -> cashFlowRow.getDate().isAfter(filterDates.get(cashFlowWithCurrency.getCurrency()).minusDays(1)))
                    .min(Comparator.comparing(CashFlowRow::getTotal))
                    .orElse(new CashFlowRow()).getTotal();  // TODO: 20.6.19 is it correct usage?
            minimalTotals.add(Pair.of(minTotal, cashFlowWithCurrency.getCurrency()));
        }
//        minimalTotals.get(0).getFirst();
//        minimalTotals.get(0).getSecond();
        return minimalTotals;
    }

    public List<CashFlowRow> getCashFlow(long idCurrency) { // TODO: 6/11/19 refactor this with lambdas
//        List<OperationRow> operationRows = operationRowRepository.findAllByIdCurrencyOrderByDateAsc(idCurrency).stream()
//                .filter(operationRow -> operationRow.getIdCashFlowDirection() != OperationRowDirection.BUDGET)
//                .filter(operationRow -> operationRow.getIdCashFlowDirection() != OperationRowDirection.GOAL)
//                .collect(Collectors.toList());

        //sort: for one day income goes first, outcome goes second
//        operationRows.sort(new Comparator<OperationRow>() { // TODO: 10.6.19 replace by lambda and make it service operation
//            @Override
//            public int compare(OperationRow o1, OperationRow o2) { //make sure that it sorted by date
//                int result = o1.getDate().compareTo(o2.getDate());
//
//                if (result == 0) { //if it is one date, than income goes first
////                    if ((o1.getIdCashFlowDirection() == OperationRowDirection.PLAN_INCOME || o1.getIdCashFlowDirection() == OperationRowDirection.INCOME)
////                            && (o2.getIdCashFlowDirection() == OperationRowDirection.PLAN_OUTCOME || o2.getIdCashFlowDirection() == OperationRowDirection.OUTCOME)) {
////                        result = -1;
////                    } else if ((o1.getIdCashFlowDirection() == OperationRowDirection.PLAN_OUTCOME || o1.getIdCashFlowDirection() == OperationRowDirection.OUTCOME)
////                            && (o2.getIdCashFlowDirection() == OperationRowDirection.PLAN_INCOME || o2.getIdCashFlowDirection() == OperationRowDirection.INCOME)) {
////                        result = 1;
////                    } else {
////                        result = 0;
////                    }
//                }
//                return result;
//            }
//        });

        List<CashFlowRow> cashFlowRows = new ArrayList<>();
        double previousRowTotal = 0;
//        for (OperationRow operationRow : operationRows) {
//            CashFlowRow cashFlowRow = new CashFlowRow();
//            cashFlowRow.setOperationRow(operationRow);
//            cashFlowRow.setId((long) cashFlowRows.size() + 1);
//            if (cashFlowRows.size() > 0) {
//                previousRowTotal = cashFlowRows.get(cashFlowRows.size() - 1).getTotal();
//            }
////            if (cashFlowRow.getOperationRow().getIdCashFlowDirection() == OperationRowDirection.INCOME
////                    || cashFlowRow.getOperationRow().getIdCashFlowDirection() == OperationRowDirection.PLAN_INCOME) {
////                cashFlowRow.setTotal(previousRowTotal + cashFlowRow.getOperationRow().getSum());
////            } else if (cashFlowRow.getOperationRow().getIdCashFlowDirection() == OperationRowDirection.OUTCOME
////                    || cashFlowRow.getOperationRow().getIdCashFlowDirection() == OperationRowDirection.PLAN_OUTCOME) {
////                cashFlowRow.setTotal(previousRowTotal - cashFlowRow.getOperationRow().getSum());
////            }
//            // TODO: 8.4.18 round total value !
//            cashFlowRow.setTotal(Math.round(cashFlowRow.getTotal()));
//            cashFlowRows.add(cashFlowRow);
//        }
        return cashFlowRows;
    }
}
