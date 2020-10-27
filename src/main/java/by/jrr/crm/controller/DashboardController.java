package by.jrr.crm.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.crm.bean.NoteItem;
import by.jrr.crm.bean.Task;
import by.jrr.crm.common.CrmCommand;
import by.jrr.crm.service.HistoryItemService;
//import by.jrr.crm.service.TgUserListService;
//import by.jrr.telegram.bot.service.TgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class DashboardController {
    @Autowired
    HistoryItemService historyItemService;
    @Autowired
    UserDataToModelService userDataToModelService;
//    @Autowired
//    TgUserListService tgUserListService;

    @GetMapping(Endpoint.CRM_DASHBOARD)
    public ModelAndView saveNewItem() {
        ModelAndView modelAndView = userDataToModelService.setData(new ModelAndView());
        modelAndView.setViewName(View.CRM_DASHBOARD);
        modelAndView.addObject("taskList", historyItemService.findAllNotFinishedTasks());

//        modelAndView.addObject("tgUsers", tgUserListService.findProfilesWithTgUser());

        return modelAndView;
    }
}
