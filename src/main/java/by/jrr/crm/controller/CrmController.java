package by.jrr.crm.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.balance.bean.OperationRow;
import by.jrr.balance.constant.Action;
import by.jrr.balance.constant.FieldName;
import by.jrr.balance.service.OperationCategoryService;
import by.jrr.balance.service.OperationRowService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.profile.repository.SubscriptionsStatisticViewDto;
import by.jrr.profile.service.ProfileService;
import by.jrr.profile.service.StreamAndTeamSubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CrmController {

    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    ProfileService profileService;

    @Autowired
    OperationRowService operationRowService;
    @Autowired
    OperationCategoryService operationCategoryService;

    @Autowired
    StreamAndTeamSubscriberService streamAndTeamSubscriberService;

    @GetMapping(Endpoint.CRM)
    public ModelAndView openCrm() {
        ModelAndView modelAndView = userDataToModelService.setData(new ModelAndView());
        modelAndView.setViewName(View.CRM);

        return modelAndView;
    }


    @GetMapping(Endpoint.CRM_STREAM)
    public ModelAndView openStreams() {
        ModelAndView modelAndView = userDataToModelService.setData(new ModelAndView());
        modelAndView.setViewName(View.CRM_PAGE_STREAMS);

        List<SubscriptionsStatisticViewDto> result = streamAndTeamSubscriberService.getSubscriptionsStatistic().stream()
                .sorted(Comparator.comparing(s -> s.getTitle()))
                .collect(Collectors.toList());

        modelAndView.addObject("streams", result);

        modelAndView.addObject("openForEnrollStreams", result.stream()
                .filter(s -> s.getOpen_for_enroll() != null)
                .filter(s -> s.getOpen_for_enroll().equals(true))
                .collect(Collectors.toList()));

        modelAndView.addObject("ongoingStreams", result.stream()
                .filter(s -> s.getDate_start() != null)
                .filter(s -> s.getDate_start().isBefore(LocalDate.now().minusDays(1)))
                .filter(s -> s.getDate_end() == null || s.getDate_end().isAfter(LocalDate.now().plusDays(1)))
                .collect(Collectors.toList()));

        return modelAndView;
    }


    @GetMapping(Endpoint.CRM_DEBTOR)
    public ModelAndView openDebtors() {
        ModelAndView modelAndView = userDataToModelService.setData(new ModelAndView());
        modelAndView.setViewName(View.CRM_PAGE_DEBTORS);

        modelAndView.addObject("streams", profileService.findAllStreamGroups()); //todo: not working without this
        modelAndView.addObject("debtors", operationRowService.getAllDebtors());

        return modelAndView;
    }


    @GetMapping(Endpoint.CRM_CASHFLOW)
    public ModelAndView openCashFlow() {
        ModelAndView modelAndView = userDataToModelService.setData(new ModelAndView());
        modelAndView.setViewName(View.CRM_PAGE_CASH_FLOW);

        modelAndView.addObject("Action", new Action());
        modelAndView.addObject("FieldName", new FieldName());
        modelAndView.addObject("blankRow", new OperationRow());

        modelAndView.addObject("operationRows", operationRowService.getAllOperationsForPeriod());
        modelAndView.addObject("total", operationRowService.summariesForAll());
        modelAndView.addObject("operationCategories", operationCategoryService.getAllOperationCategories());

        return modelAndView;
    }


}
