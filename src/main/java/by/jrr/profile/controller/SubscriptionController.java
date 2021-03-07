package by.jrr.profile.controller;

import by.jrr.auth.configuration.annotations.AccessAdminAndSales;
import by.jrr.auth.configuration.annotations.AdminOnly;
import by.jrr.constant.Endpoint;
import by.jrr.profile.mapper.SubscriptionMapper;
import by.jrr.profile.model.SubscriptionDto;
import by.jrr.profile.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static by.jrr.common.MyHeaders.cameFrom;

@Controller
public class SubscriptionController {

    @Autowired
    SubscriptionService subscriptionService;

    @PostMapping(Endpoint.SUBSCRIPTIONS_REQUEST)
    public String requestSubscription(@RequestParam Map<String,String> paramMap, HttpServletRequest request) {
        SubscriptionDto.Request subscReq = SubscriptionMapper.OF.getSubscriptionRequestFromMap(paramMap);
        request.getSession().setAttribute("notification", subscriptionService.requestSubscription(subscReq).getNotes());
        return "redirect:".concat(cameFrom(request));
    }

    @AccessAdminAndSales
    @PostMapping(Endpoint.SUBSCRIPTIONS_APPROVE)
    public String approveSubscription(@RequestParam Map<String,String> paramMap, HttpServletRequest request) {
        SubscriptionDto.Request subscReq = SubscriptionMapper.OF.getSubscriptionRequestFromMap(paramMap);
        request.getSession().setAttribute("notification", subscriptionService.approveSubscription(subscReq).getNotes());
        return "redirect:".concat(cameFrom(request));

    }

    @AccessAdminAndSales
    @PostMapping(Endpoint.SUBSCRIPTIONS_REJECT)
    public String rejectSubscription(@RequestParam Map<String,String> paramMap, HttpServletRequest request) {
        SubscriptionDto.Request subscReq = SubscriptionMapper.OF.getSubscriptionRequestFromMap(paramMap);
        request.getSession().setAttribute("notification", subscriptionService.rejectSubscription(subscReq).getNotes());
        return "redirect:".concat(cameFrom(request));
    }

    @PostMapping(Endpoint.SUBSCRIPTIONS_UNSUBSCRIBE)
    public String unsubscribe(@RequestParam Map<String,String> paramMap, HttpServletRequest request) {
        SubscriptionDto.Request subscReq = SubscriptionMapper.OF.getSubscriptionRequestFromMap(paramMap);
        request.getSession().setAttribute("notification", subscriptionService.unsubscribe(subscReq).getNotes());
        return "redirect:".concat(cameFrom(request));
    }
}
