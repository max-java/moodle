package by.jrr.balance.controller;

import by.jrr.auth.configuration.annotations.AdminOnly;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.balance.bean.OperationCategory;
import by.jrr.balance.constant.Action;
import by.jrr.balance.constant.FieldName;
import by.jrr.balance.constant.FormCommand;
import by.jrr.balance.constant.OperationRowDirection;
import by.jrr.balance.service.OperationCategoryService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static by.jrr.common.MyHeaders.cameFrom;

// TODO: 05/10/2020 move this to crm package

@Controller
@AdminOnly
public class OperationCategoryController {

    @Autowired
    OperationCategoryService operationCategoryService;
    @Autowired
    UserDataToModelService userDataToModelService;

    @PostMapping(Action.ADD_OPERATION_CATEGORY)
    public String editOperationRowAction(@RequestParam(value = FieldName.ID_OPERATION_CATEGORY, required = false) Long idOperationCategory,
                                         @RequestParam(value = FieldName.OPERATION_CATEGORY_NAME, required = true) String categoryName,
                                         @RequestParam(value = FieldName.OPERATION_DIRECTION, required = false) OperationRowDirection operationDirection,
                                         @RequestParam(value = "formCommand", required = true) FormCommand formCommand,
                                         HttpServletRequest request) {

        switch (formCommand) {
            case SAVE:
                operationCategoryService.save(OperationCategory.builder()
                    .id(idOperationCategory)
                    .name(categoryName)
                    .operationRowDirection(operationDirection)
                    .build());
                break;
        }

        return "redirect:".concat(cameFrom(request));
    }

    @GetMapping(Endpoint.CRM_OPERATION_CATEGORIES)
    public ModelAndView openOperationCategoryList() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.CRM_OPERATION_CATEGORIES);
        mov.addObject("emptyOperationCategory", new OperationCategory());
        mov.addObject("Action", new Action());
        mov.addObject("FieldName", new FieldName());
        mov.addObject("operationCategoryList", operationCategoryService.getAllOperationCategories());

        return mov;
    }

}
