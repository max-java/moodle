package by.jrr.balance.service;

import by.jrr.balance.bean.*;
import by.jrr.balance.bean.Currency;
import by.jrr.balance.currency.service.CurrencyService;
import by.jrr.balance.dto.UserBalanceSummaryDto;
import by.jrr.balance.repository.OperationRowRepository;
import by.jrr.balance.constant.FieldName;
import by.jrr.balance.constant.OperationRowDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static by.jrr.balance.bean.Currency.*;

/**
 * Created by Shelkovich Maksim on 23.4.18.
 */
@Service
public class OperationRowService {

    private final Logger logger = LoggerFactory.getLogger(OperationRowService.class);

    @Autowired
    OperationRowRepository operationRowRepository;
    @Autowired
    GoalService goalService;
    @Autowired
    OperationToProfileService operationToProfileService;
    @Autowired
    OperationCategoryService operationCategoryService;
    @Autowired
    CurrencyService currencyService;

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

    /**
     * Is different than usual summary for streams, because it should calculate all payments in contract currency,
     * if paid in other - than it should be converted to contract currency,
     * than calculate summary for currency,
     * than convert total balance into common currency on current date.
     * Student balance is a sum of contract balances;
     *
     * @param contracts
     * @return SummaryOperations
     */
    public UserBalanceSummaryDto summariesForUserOperations(List<Contract> contracts, Currency inCurrency) {
        Map<Currency, List<Contract>> contractMap = groupContractsByCurrency(contracts);
        setOperationsForContractMap(contractMap);

        UserBalanceSummaryDto userBalanceSummaryDto = new UserBalanceSummaryDto();
        userBalanceSummaryDto.setCurrency(inCurrency);
        userBalanceSummaryDto.setSummaryOperations(createSummariesInContractsCurrency(contractMap));

        Map<Currency, List<SummaryOperations>> summaryListsGroupedByCurrency
                = groupSummaryOperationsByCurrency(userBalanceSummaryDto.getSummaryOperations());

        Map<Currency, SummaryOperations> summariesGroupedAndReduced = new HashMap<>();
        summariesGroupedAndReduced.put(BYN, reduceListSummaryOperations(summaryListsGroupedByCurrency.get(BYN), BYN));
        summariesGroupedAndReduced.put(USD, reduceListSummaryOperations(summaryListsGroupedByCurrency.get(USD), USD));
        userBalanceSummaryDto.setSummaryGroupedByCurrencies(summariesGroupedAndReduced);

        userBalanceSummaryDto.setSummaryInCurrentCurrency(convertInCommonCurrencyAndCalculateDebt(
                userBalanceSummaryDto.getSummaryGroupedByCurrencies(),
                userBalanceSummaryDto.getCurrency()));

        return userBalanceSummaryDto;
    }

    private Map<Currency, List<Contract>> groupContractsByCurrency(List<Contract> contracts) {
        return contracts.stream()
                .collect(Collectors.groupingBy(
                        Contract::getCurrency,
                        Collectors.mapping(Function.identity(), Collectors.toList())
                ));
    }

    private List<SummaryOperations> createSummariesInContractsCurrency(Map<Currency, List<Contract>> contractMap) {
        final List<SummaryOperations> summaryOperations = new ArrayList<>();
        contractMap.forEach((currency, contractsList) -> contractsList.forEach(contract ->
                summaryOperations.add(createSummaryForContractInContractCurrency(contract))
        ));
        return summaryOperations;
    }


    private void setOperationsForContractMap(Map<Currency, List<Contract>> contractMap) {
        contractMap.forEach((curr, contractList) -> contractList
                .forEach(contract -> contract.setOperations(
                        operationRowRepository.findAllByIdIn(
                                operationToProfileService.getIdOperationsForContractId(contract.getId())))));
    }

    private SummaryOperations createSummaryForContractInContractCurrency(final Contract contract) {
        SummaryOperations summaryOperations = new SummaryOperations();
        summaryOperations.setCurrency(contract.getCurrency());

        summaryOperations.setContract(contract.getSum());

        summaryOperations.setIncome(
                contract.getOperations().stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.INCOME))
                        .map(o -> currencyService.convertAndGetOperationRowSum(o, contract.getCurrency()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        summaryOperations.setInvoice(
                contract.getOperations().stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.INVOICE))
                        .map(o -> currencyService.convertAndGetOperationRowSum(o, contract.getCurrency()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        return summaryOperations;
    }

    private Map<Currency, List<SummaryOperations>> groupSummaryOperationsByCurrency(List<SummaryOperations> summaryOperations) {
        return
                summaryOperations.stream()
                        .collect(Collectors.groupingBy(
                                SummaryOperations::getCurrency,
                                Collectors.mapping(Function.identity(), Collectors.toList())
                        ));
    }

    private SummaryOperations reduceListSummaryOperations(List<SummaryOperations> summaryOperations, Currency currency) {
        if(summaryOperations == null) {
            logger.debug("summaries null");
            summaryOperations = new ArrayList<>();
        }

        SummaryOperations summaryOperationsCombined = new SummaryOperations();

        summaryOperationsCombined.setCurrency(currency);

        summaryOperationsCombined.setContract(summaryOperations.stream()
                .map(summ -> summ.getContract())
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        summaryOperationsCombined.setIncome(summaryOperations.stream()
                .map(summ -> summ.getIncome())
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        summaryOperationsCombined.setInvoice(summaryOperations.stream()
                .map(summ -> summ.getInvoice())
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        summaryOperationsCombined.setUserBalance();

        return summaryOperationsCombined;
    }

    private SummaryOperations convertInCommonCurrencyAndCalculateDebt(final Map<Currency, SummaryOperations> summaryOperationsMap, final Currency currency) {
        SummaryOperations summaryOperationsCombined = new SummaryOperations();

        summaryOperationsCombined.setContractDebt(summaryOperationsMap.values().stream()
                .map(summ -> currencyService.convertOnToday(summ.getContractDebt(), summ.getCurrency(), currency))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        summaryOperationsCombined.setInvoiceDebt(summaryOperationsMap.values().stream()
                .map(summ -> currencyService.convertOnToday(summ.getInvoiceDebt(), summ.getCurrency(), currency))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return summaryOperationsCombined;
    }

    /**
     * Foreign currency transactions are counted with the rate of the National Bank
     * on the date of contract/act/payment.
     * <p>
     * The difference in exchange rates is considered as non-release income / expenses
     *
     * @param streamId
     * @return
     */
    public SummaryOperations summariesForStream(Long streamId) {
        List<OperationRow> operations = operationRowRepository.findAllByIdIn(
                operationToProfileService.getIdOperationsForStreamById(streamId));

        SummaryOperations summaryOperations = new SummaryOperations();

        summaryOperations.setIncome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.INCOME))
                        .map(o -> currencyService.getOperationRowSumInByn(o))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setOutcome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.OUTCOME))
                        .map(o -> currencyService.getOperationRowSumInByn(o))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setContract(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.CONTRACT))
                        .map(o -> currencyService.getOperationRowSumInByn(o))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setInvoice(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.INVOICE))
                        .map(o -> currencyService.getOperationRowSumInByn(o))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setPlanIncome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.PLAN_INCOME))
                        .map(o -> currencyService.getOperationRowSumInByn(o))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setPlanOutcome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.PLAN_OUTCOME))
                        .map(o -> currencyService.getOperationRowSumInByn(o))
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
                        .map(o -> currencyService.getOperationRowSumInByn(o))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setOutcome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.OUTCOME))
                        .map(o -> currencyService.getOperationRowSumInByn(o))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setContract(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.CONTRACT))
                        .map(o -> currencyService.getOperationRowSumInByn(o))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setInvoice(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.INVOICE))
                        .map(o -> currencyService.getOperationRowSumInByn(o))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setPlanIncome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.PLAN_INCOME))
                        .map(o -> currencyService.getOperationRowSumInByn(o))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        summaryOperations.setPlanOutcome(
                operations.stream()
                        .filter(o -> o.getOperationRowDirection().equals(OperationRowDirection.PLAN_OUTCOME))
                        .map(o -> currencyService.getOperationRowSumInByn(o))
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
