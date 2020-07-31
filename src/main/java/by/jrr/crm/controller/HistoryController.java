package by.jrr.crm.controller;

import by.jrr.constant.Endpoint;
import by.jrr.crm.bean.HistoryItem;
import by.jrr.crm.service.HistoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
public class HistoryController {
    @Autowired
    HistoryItemService historyItemService;

    @PostMapping(Endpoint.CRM_NEW_HISTORY_ITEM)
    public ModelAndView saveNewItem(@RequestParam Long profileId,
                                    @RequestParam String message) {
        historyItemService.saveHistoryForProfile(HistoryItem.builder()
                .profileId(profileId)
                .text(message)
                .timestamp(LocalDateTime.now())
                .build());
        return new ModelAndView("redirect:" + Endpoint.PROFILE_CARD + "/" + profileId);
    }


}
