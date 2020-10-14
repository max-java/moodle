package by.jrr.crm.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.balance.bean.OperationCategory;
import by.jrr.balance.bean.OperationRow;
import by.jrr.balance.constant.Action;
import by.jrr.balance.constant.FieldName;
import by.jrr.balance.service.OperationRowService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CrmController {

    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    ProfileService profileService;

    @Autowired
    OperationRowService operationRowService;

    @GetMapping(Endpoint.CRM)
    public ModelAndView saveNewItem() {
        ModelAndView modelAndView = userDataToModelService.setData(new ModelAndView());
        modelAndView.setViewName(View.CRM);

        modelAndView.addObject("streams", profileService.findAllStreamGroups());

        modelAndView.addObject("Action", new Action());
        modelAndView.addObject("FieldName", new FieldName());

        //blank instances for forms can work to add
        modelAndView.addObject("blankRow", new OperationRow());

        //billing
        modelAndView.addObject("blankRow", new OperationRow());
        modelAndView.addObject("total", operationRowService.sumForAll());

        //operation category
        modelAndView.addObject("operationCategory", new OperationCategory()); // TODO: 14/10/2020 could be deleted

        return modelAndView;
    }


}
