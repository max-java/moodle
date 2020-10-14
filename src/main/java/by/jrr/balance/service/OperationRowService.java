package by.jrr.balance.service;

import by.jrr.balance.bean.*;
import by.jrr.balance.repository.OperationRowRepository;
import by.jrr.balance.constant.FieldName;
import by.jrr.balance.constant.OperationRowDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by Shelkovich Maksim on 23.4.18.
 */
@Service
public class OperationRowService {

    @Autowired
    OperationRowRepository operationRowRepository;
    @Autowired
    GoalService goalService;
    @Autowired
    OperationToProfileService operationToProfileService;

    public void deleteRow(Long id) {
        operationRowRepository.deleteById(id);
    }

    public OperationRow saveRow(OperationRow row) {
        return operationRowRepository.save(row);
    }

    // TODO: 05/10/2020 delete after debugging
    public List<OperationRow> getAllAsIs() {
        return (List) operationRowRepository.findAll();

    }

    public SummaryOperations sumForStream(Long streamId) {
        List<OperationRow> operations = operationRowRepository.findAllByIdIn(
                operationToProfileService.getIdOperationsForStreamById(streamId));

        SummaryOperations summaryOperations = new SummaryOperations();

        summaryOperations.setIncome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.INCOME))
                        .map(OperationRow::getSum)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setOutcome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.OUTCOME))
                        .map(OperationRow::getSum)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        summaryOperations.calculateProfit();
        return summaryOperations;
    }

    public SummaryOperations sumForAll() {
        List<OperationRow> operations = (List) operationRowRepository.findAll();

        SummaryOperations summaryOperations = new SummaryOperations();

        summaryOperations.setIncome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.INCOME))
                        .map(OperationRow::getSum)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setOutcome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.OUTCOME))
                        .map(OperationRow::getSum)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        summaryOperations.calculateProfit();
        return summaryOperations;
    }

    public List<OperationRow> getOperationsForStream(Long streamId) {
        return operationRowRepository.findAllByIdIn(
                operationToProfileService.getIdOperationsForStreamById(streamId)).stream()
                .peek(this::setSubscriberToOperationRow)
                .peek(this::setContractToOperationRow)
                .collect(Collectors.toList());
    }

    private void setContractToOperationRow(OperationRow operationRow) {
        operationRow.setContract(
                operationToProfileService.getContractForOperationByOperationId(operationRow.getId()));
    }

    private void setSubscriberToOperationRow(OperationRow operationRow) {
        operationRow.setSubscriber(
                operationToProfileService.getSubscriberProfileForOperationByOperationId(operationRow.getId()));
    }

    public List<OperationRow> getIncomesForContract(Long contractId) {
        return operationRowRepository.findAllByIdIn(
                operationToProfileService.getIdOperationsForContractId(contractId)).stream()
                .filter(op -> op.getOperationRowDirection().equals(OperationRowDirection.INCOME))
                .collect(Collectors.toList());
    }

    public List<OperationRow> getIncomesWithoutContract() {
        return operationRowRepository.findAllByIdIn(
                operationToProfileService.getIdOperationsWhereContractIsNull()).stream()
                .filter(op -> op.getOperationRowDirection().equals(OperationRowDirection.INCOME))
                .collect(Collectors.toList());
    }


    // TODO: 23.4.18 if change repeat N times, than data changes. need to create new Instances, not change.
    // TODO: 6/7/19 split updating repeatable and nonRepeatable operations
    public List<OperationRow> actionEditOperationRow(Long id,
                                                     OperationRowDirection operationRowDirection,
                                                     LocalDate date,
                                                     Double sum,
                                                     Currency currency,
                                                     Long idOperationCategory,
                                                     String note,
                                                     Integer repeatNTimes,
                                                     String repeatingFrequency,
                                                     String endOfRepeatingDate) {

        List<OperationRow> operationRowList = new ArrayList<>();    // need array list for repeatable operation todo: proceed repeatable operation separately
        int repeatableToken = generateRandom();                     // generate uniq token for repeatable rows.

        // for repeatable depend on N times
        if (endOfRepeatingDate.equals("")) { // TODO: 05/10/2020 split this: single add should be single, generated should be generated
            for (int i = 0; i < repeatNTimes; i++) {
                OperationRow row = new OperationRow();
                row.setId(id);
                if (repeatingFrequency.equals(FieldName.REPEAT_MONTHLY)) {
                    row.setDate(date.plusMonths(i)); // for first cycle i = 0, => month don't added
                } else if (repeatingFrequency.equals(FieldName.REPEAT_DAILY)) {
                    row.setDate(date.plusDays(i)); // for first cycle i = 0, => days don't added
                } else { // TODO: 6/7/19 if frequency selected NONE or didn't selected at all (but controller sets default NONE)
                    row.setDate(date);
                }
                row.setSum(sum);
                row.setCurrency(currency);
                row.setOperationRowDirection(operationRowDirection);
                row.setNote(note);
                row.setRepeatableToken(repeatableToken);
                operationRowList.add(row);
            }
            return (List) operationRowRepository.saveAll(operationRowList);

            // for repeatable depend on date period
        } else if (!endOfRepeatingDate.equals("")) { // TODO: 6/7/19 debug this clause
            int i = 0;
            LocalDate endOfRepeatableOperationDate = LocalDate.parse(endOfRepeatingDate).plusDays(1); //create last operation date add one day to achieve less or equal in the while cycle
            if (repeatingFrequency.equals(FieldName.REPEAT_MONTHLY)) {
                while (date.plusMonths(i).isBefore(endOfRepeatableOperationDate)) {
                    OperationRow row = new OperationRow();
                    row.setId(id);
//                    row.setIdCashFlowDirection(idCashFlowDirection);
                    row.setDate(date.plusMonths(i)); // for first cycle i = 0, => month don't added
                    row.setSum(sum);
//                    row.setIdCurrency(currencyId);
//                    row.setIdOperationCategory(idOperationCategory);
                    row.setNote(note);
                    row.setRepeatableToken(repeatableToken);
                    operationRowList.add(row);
                    i++;
                }
            } else if (repeatingFrequency.equals(FieldName.REPEAT_DAILY)) {
                while (date.plusDays(i).isBefore(endOfRepeatableOperationDate)) {
                    OperationRow row = new OperationRow();
                    row.setId(id);
//                    row.setIdCashFlowDirection(idCashFlowDirection);
                    row.setDate(date.plusDays(i)); // for first cycle i = 0, => days don't added
                    row.setSum(sum);
//                    row.setIdCurrency(currencyId);
//                    row.setIdOperationCategory(idOperationCategory);
                    row.setNote(note);
                    row.setRepeatableToken(repeatableToken);
                    operationRowList.add(row);
                    i++;
                }
            } else { // TODO: 6/7/19 if frequency selected NONE or didn't selected at all (but controller sets default NONE)
                // TODO: 05/10/2020 this is never'll be called
                OperationRow row = OperationRow.builder()
                        .id(id)
                        .date(date)
                        .sum(BigDecimal.valueOf(sum))
                        .currency(currency)
                        .operationRowDirection(operationRowDirection)
                        .note(note)
                        .build();
                operationRowList.add(row);
                i++;


            }
            return (List) operationRowRepository.saveAll(operationRowList);
            // TODO: 6/7/19 delete code duplication
        }
        return null;
    }

    /**
     * this moves operation row from plan to fact by replacing idCashFlowDirection and writing fact values for date, sum, currency and note (works only for single operation)
     */
    public void moveRowFromPlanToFact(Long id,
                                      LocalDate date,
                                      double sum,
                                      Currency currency,
                                      String note) {

        //get operationRow from database
        OperationRow row = operationRowRepository.findById(id).get();
        //proceed only for change from plan to fact
        // todo add changing from fact to plan by:
        // todo adding if clause for OperationRowDirection.INCOME | OUTCOME and renaming button on the form
//        if (row.getIdCashFlowDirection() == OperationRowDirection.PLAN_INCOME || row.getIdCashFlowDirection() == OperationRowDirection.PLAN_OUTCOME) {
//            //update values with data from form
//            row.setDate(LocalDate.parse(date));
//            row.setSum(sum);
//            row.setIdCurrency(currencyId);
//            row.setNote(note);
//            if (row.getIdCashFlowDirection() == OperationRowDirection.PLAN_INCOME) {
//                row.setIdCashFlowDirection(OperationRowDirection.INCOME);
//            } else if (row.getIdCashFlowDirection() == OperationRowDirection.PLAN_OUTCOME) {
//                row.setIdCashFlowDirection(OperationRowDirection.OUTCOME);
//            }
//            operationRowRepository.save(row);
//        }
    }

    private Integer generateRandom() {
        Random randomGen = new Random(); // need to generate repeatable token todo no need of repeatable token, batch operations should be available only for future operations selected by note field and sum.
        List<OperationRow> operationRowList = new ArrayList<>();
        boolean randomNotUniq = true;
        int random = 0;
        while (randomNotUniq) {
            random = randomGen.nextInt();
            operationRowList = operationRowRepository.findAllByRepeatableToken(random);
            if (operationRowList.size() == 0) {
                randomNotUniq = false;
            }
        }
        return random;
    }

    public void moveRowFromPlanToGoals(Long idOperationRow) {
        goalService.moveOperationRowToGoals(idOperationRow);
    }

    private OperationRow mapFieldsToRow(Long id,
                                        OperationRowDirection operationRowDirection,
                                        LocalDate date,
                                        Double sum,
                                        Currency currency,
                                        Long idOperationCategory,
                                        String note,
                                        Integer repeatNTimes,
                                        String repeatingFrequency,
                                        String endOfRepeatingDate) {
        return OperationRow.builder()
                .id(id)
                .date(date)
                .sum(BigDecimal.valueOf(sum))
                .currency(currency)
                .operationRowDirection(operationRowDirection)
                .note(note)
                .build();
    }
}
