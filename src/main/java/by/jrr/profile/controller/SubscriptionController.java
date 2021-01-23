package by.jrr.profile.controller;

import by.jrr.common.MyHeaders;
import by.jrr.constant.Endpoint;
import by.jrr.profile.mapper.SubscriptionMapper;
import by.jrr.profile.model.SubscriptionDto;
import by.jrr.profile.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class SubscriptionController {

    @Autowired
    SubscriptionService subscriptionService;

    @PostMapping(Endpoint.SUBSCRIPTIONS_REQUEST)
    public ModelAndView requestSubscription(@RequestParam Map<String,String> paramMap, HttpServletRequest request) {
        SubscriptionDto.Request subscReq = SubscriptionMapper.OF.getSubscriptionRequestFromMap(paramMap);
        request.getSession().setAttribute("notification", subscriptionService.requestSubscription(subscReq).getNotes());
        return redirectToProfileCard(subscReq, request);
    }

    @PostMapping(Endpoint.SUBSCRIPTIONS_APPROVE)
    public ModelAndView approveSubscription(@RequestParam Map<String,String> paramMap, HttpServletRequest request) {
        SubscriptionDto.Request subscReq = SubscriptionMapper.OF.getSubscriptionRequestFromMap(paramMap);
        request.getSession().setAttribute("notification", subscriptionService.requestSubscription(subscReq).getNotes());
        return redirectToProfileCard(subscReq, request);

    }

    @PostMapping(Endpoint.SUBSCRIPTIONS_REJECT)
    public ModelAndView  rejectSubscription(@RequestParam Map<String,String> paramMap, HttpServletRequest request) {
        SubscriptionDto.Request subscReq = SubscriptionMapper.OF.getSubscriptionRequestFromMap(paramMap);
        request.getSession().setAttribute("notification", subscriptionService.requestSubscription(subscReq).getNotes());
        return redirectToProfileCard(subscReq, request);
    }

    @PostMapping(Endpoint.SUBSCRIPTIONS_UNSUBSCRIBE)
    public ModelAndView  unsubscribe(@RequestParam Map<String,String> paramMap, HttpServletRequest request) {
        SubscriptionDto.Request subscReq = SubscriptionMapper.OF.getSubscriptionRequestFromMap(paramMap);
        request.getSession().setAttribute("notification", subscriptionService.requestSubscription(subscReq).getNotes());
        return redirectToProfileCard(subscReq, request);
    }

    public ModelAndView redirectToProfileCard(SubscriptionDto.Request subscReq, HttpServletRequest request) {
        if(MyHeaders.cameFrom(request).contains(Endpoint.CRM)) {
            return new ModelAndView(String.format("redirect:%s/%s", Endpoint.PROFILE_CARD_ADMIN_VIEW, subscReq.getStreamTeamProfileId()));
        }
        return new ModelAndView(String.format("redirect:%s/%s", Endpoint.PROFILE_CARD, subscReq.getStreamTeamProfileId()));
    }

}
