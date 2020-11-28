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
import java.util.Comparator;
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
    @Autowired
    OperationCategoryService operationCategoryService;

    private Comparator<OperationRow> sortOperationRowsByDateReversed = Comparator.comparing(OperationRow::getDate).reversed();

    public void deleteRow(Long id) {
        operationRowRepository.deleteById(id);
    }

    public OperationRow saveRow(OperationRow row) {
        return operationRowRepository.save(row);
    }

    // TODO: 05/10/2020 delete after debugging
    public List<OperationRow> getAllAsIsLazy() {
        return (List) operationRowRepository.findAll();

    }

    public List<OperationRow> getAllOperationsForPeriod() { // TODO: 15/10/2020 add Period
        List<OperationRow> operations = (List) operationRowRepository.findAll();
        return operations.stream()
                .peek(this::setSubscriberToOperationRow)
                .peek(this::setContractToOperationRow)
                .peek(this::setOperationCategoryToOperationRow)
                .sorted(sortOperationRowsByDateReversed)
                .collect(Collectors.toList());
    }

    public List<OperationRow> getAllOperationsForUser(Long userProfileId) {
        List<Long> operationIds = operationToProfileService.getIdOperationsForUserByUserProfileId(userProfileId);
        List<OperationRow> operations = (List) operationRowRepository.findAllByIdIn(operationIds);
        return operations.stream()
                .peek(this::setSubscriberToOperationRow)
                .peek(this::setContractToOperationRow)
                .peek(this::setOperationCategoryToOperationRow)
                .sorted(sortOperationRowsByDateReversed)
                .collect(Collectors.toList());

    }

    public SummaryOperations summariesForUserOperations(List<OperationRow> operations) {
        SummaryOperations summaryOperations = new SummaryOperations();

        summaryOperations.setIncome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.INCOME))
                        .map(OperationRow::getSumInByn)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        summaryOperations.setContract(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.CONTRACT))
                        .map(OperationRow::getSumInByn)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        summaryOperations.setInvoice(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.INVOICE))
                        .map(OperationRow::getSumInByn)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        summaryOperations.setUserBalance();

        return summaryOperations;
    }


    public SummaryOperations summariesForStream(Long streamId) {
        List<OperationRow> operations = operationRowRepository.findAllByIdIn(
                operationToProfileService.getIdOperationsForStreamById(streamId));

        SummaryOperations summaryOperations = new SummaryOperations();

        summaryOperations.setIncome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.INCOME))
                        .map(OperationRow::getSumInByn)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setOutcome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.OUTCOME))
                        .map(OperationRow::getSumInByn)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setContract(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.CONTRACT))
                        .map(OperationRow::getSumInByn)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setInvoice(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.INVOICE))
                        .map(OperationRow::getSumInByn)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setPlanIncome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.PLAN_INCOME))
                        .map(OperationRow::getSumInByn)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setPlanOutcome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.PLAN_OUTCOME))
                        .map(OperationRow::getSumInByn)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        summaryOperations.calculateProfit();
        return summaryOperations;
    }

    public SummaryOperations summariesForAll() {
        List<OperationRow> operations = (List) operationRowRepository.findAll();

        SummaryOperations summaryOperations = new SummaryOperations();

        summaryOperations.setIncome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.INCOME))
                        .map(OperationRow::getSumInByn)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setOutcome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.OUTCOME))
                        .map(OperationRow::getSumInByn)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setContract(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.CONTRACT))
                        .map(OperationRow::getSumInByn)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setInvoice(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.INVOICE))
                        .map(OperationRow::getSumInByn)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setPlanIncome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.PLAN_INCOME))
                        .map(OperationRow::getSumInByn)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setPlanOutcome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.PLAN_OUTCOME))
                        .map(OperationRow::getSumInByn)
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
                .peek(this::setOperationCategoryToOperationRow)
                .sorted(sortOperationRowsByDateReversed)
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
    private void setOperationCategoryToOperationRow(OperationRow operationRow) {
        operationRow.setOperationCategory(
                operationCategoryService.findOperationCategoryById(operationRow.getIdOperationCategory()));
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
                row.setIdOperationCategory(idOperationCategory);
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
                    row.setOperationRowDirection(operationRowDirection);
                    row.setDate(date.plusMonths(i)); // for first cycle i = 0, => month don't added
                    row.setSum(sum);
                    row.setCurrency(currency);
                    row.setOperationRowDirection(operationRowDirection);
                    row.setIdOperationCategory(idOperationCategory);
                    row.setNote(note);
                    row.setRepeatableToken(repeatableToken);
                    operationRowList.add(row);
                    i++;
                }
            } else if (repeatingFrequency.equals(FieldName.REPEAT_DAILY)) {
                while (date.plusDays(i).isBefore(endOfRepeatableOperationDate)) {
                    OperationRow row = new OperationRow();
                    row.setId(id);
                    row.setOperationRowDirection(operationRowDirection);
                    row.setDate(date.plusDays(i)); // for first cycle i = 0, => days don't added
                    row.setSum(sum);
                    row.setCurrency(currency);
                    row.setOperationRowDirection(operationRowDirection);
                    row.setIdOperationCategory(idOperationCategory);
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
