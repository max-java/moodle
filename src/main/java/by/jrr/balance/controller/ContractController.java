package by.jrr.balance.controller;

import by.jrr.auth.configuration.annotations.AdminOnly;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.balance.bean.*;
import by.jrr.balance.constant.Action;
import by.jrr.balance.constant.FieldName;
import by.jrr.balance.service.ContractService;
import by.jrr.balance.service.OperationRowService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;

import static by.jrr.common.MyHeaders.cameFrom;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

// TODO: 05/10/2020 move this to crm package

@Controller
@AdminOnly
public class ContractController {

    @Autowired
    ContractService contractService;

    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    OperationRowService operationRowService;

    @GetMapping(Endpoint.CRM_CONTRACTS)
    public ModelAndView openContractsDashboard() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.CRM_CONTRACTS);
        mov.addObject("type", new ContractType());
        mov.addObject("FieldName", new FieldName());
        mov.addObject("Action", new Action());
        mov.addObject("contractTypes", contractService.getContractTypes());
        mov.addObject("contracts", contractService.findAllContracts());
        mov.addObject("orphanIncomes", operationRowService.getIncomesWithoutContract());
        return mov;
    }

    @PostMapping(Action.ADD_CONTRACT_TYPE) // TODO: 11/10/2020 create contractTypePage with edit text thymeleaf textarea
    public String editContractType(@RequestParam(value = FieldName.CONTRACT_TYPE_ID, required = false) Long contractId,
                                   @RequestParam(FieldName.CONTRACT_TYPE_NAME) String contractName,
                                   @RequestParam(value = FieldName.CONTRACT_TYPE_EFFECTIVE_DATE) @DateTimeFormat(iso = DATE) LocalDate effectiveDate,
                                   @RequestParam(FieldName.TEXT) String text,
                                   HttpServletRequest request
    ) {
        ContractType contractType = ContractType.builder()
                .id(contractId)
                .effectiveDate(effectiveDate)
                .name(contractName)
                .text(text)
                .build();
        contractService.save(contractType);

        return "redirect:".concat(cameFrom(request));
    }

    @PostMapping(Action.ADD_CONTRACT)
    public String editContract(@RequestParam(value = FieldName.ID_CONTRACT, required = false) Long contractId,
                               @RequestParam(value = FieldName.CONTRACT_DATE) @DateTimeFormat(iso = DATE) LocalDate date,
                               @RequestParam(FieldName.CONTRACT_NUMBER) String contractNumber,
                               @RequestParam(FieldName.CONTRACT_SUM) Double contractSum,
                               @RequestParam(FieldName.ID_CURRENCY) Currency currency,
                               @RequestParam(FieldName.CONTRACT_TYPE_ID) Long contractTypeId,
                               @RequestParam(FieldName.Subscriber) Long subscriberId,
                               @RequestParam(FieldName.PROFILE_ID) Long streamId,

                               @RequestParam(FieldName.ACCEPTANCE_ACT_ID) Long acId,
                               @RequestParam(FieldName.ACCEPTANCE_ACT_DATE) @DateTimeFormat(iso = DATE) LocalDate acDate,
                               @RequestParam(FieldName.ACCEPTANCE_ACT_NUMBER) String acNumber,
                               @RequestParam(FieldName.ACCEPTANCE_ACT_SUM) Double acSum,
                               @RequestParam(FieldName.ACCEPTANCE_ACT_CONTRACT_ID) Long acContractId,
                               @RequestParam(FieldName.ACCEPTANCE_ACT_CURRENCY) Currency acCurrency,

                               @RequestParam(value = "command", required = false) String command,
                               HttpServletRequest request
    ) {

        Contract contract = Contract.builder()
                .id(contractId)
                .date(date)
                .number(contractNumber)
                .sum(setBitGecimal(contractSum))
                .currency(currency)
                .contractTypeId(contractTypeId)
                .build();

        AcceptanceAct acceptanceAct = AcceptanceAct.builder()
                .id(acId)
                .date(acDate)
                .number(acNumber)
                .sum(setBitGecimal(acSum))
                .contractId(acContractId)
                .currency(acCurrency)
                .build();

        contractService.save(contract, streamId, subscriberId, acceptanceAct);

        return "redirect:".concat(cameFrom(request));
    }

    private BigDecimal setBitGecimal(Double val) {
        try {
            return BigDecimal.valueOf(val);
        } catch (Exception ex) {
            // TODO: 12/10/2020 log exception.
            return null;
        }
    }
}
