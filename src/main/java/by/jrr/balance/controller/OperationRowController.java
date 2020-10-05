package by.jrr.balance.controller;

import by.jrr.balance.bean.Currency;
import by.jrr.balance.constant.Action;
import by.jrr.balance.constant.FieldName;
import by.jrr.balance.constant.OperationRowDirection;
import by.jrr.balance.service.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

import static by.jrr.common.MyHeaders.cameFrom;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

// TODO: 05/10/2020 move this to crm package

@Controller
public class OperationRowController {

    @Autowired
    Main main;

    @PostMapping(Action.ADD_OPERATION_ROW)
    public String editOperationRowAction(@RequestParam(value = FieldName.ID_OPERATION, required = false) Long idOperation,
                                         @RequestParam(FieldName.DIRECTION) OperationRowDirection operationDirection,
                                         @RequestParam(value = FieldName.OPERATION_DATE) @DateTimeFormat(iso = DATE) LocalDate date,
                                         @RequestParam(FieldName.OPERATION_SUM) Double sum,
                                         @RequestParam(FieldName.ID_CURRENCY) Currency currency,
                                         @RequestParam(FieldName.ID_OPERATION_CATEGORY) Long idOperationCategory,
                                         @RequestParam(FieldName.OPERATION_NOTE) String note,
                                         @RequestParam(value = FieldName.PROFILE_ID, required = false) Long streamProfileId,
                                         @RequestParam(value = FieldName.Subscriber, required = false) Long subscriberId,
                                         @RequestParam(value = FieldName.REPEAT_N_TIMES, defaultValue = "1") Integer repeatNTimes,
                                         @RequestParam(value = FieldName.REPEAT_RADIO, defaultValue = FieldName.REPEAT_NONE) String repeatingFrequency,
                                         @RequestParam(value = FieldName.END_OF_REPEATING_DATE, required = false) String endOfRepeatingDate,
                                         @RequestParam(value = "submit", required = false) String submit,
                                         HttpServletRequest request
    ) {

        main.processOperationRowForm(
                streamProfileId,
                subscriberId,
                idOperation,
                operationDirection,
                date,
                sum,
                currency,
                idOperationCategory,
                note,
                repeatNTimes,
                repeatingFrequency,
                endOfRepeatingDate,
                submit
        );

        return "redirect:".concat(cameFrom(request));
    }
}
