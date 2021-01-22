package by.jrr.profile.controller;

import by.jrr.common.MyHeaders;
import by.jrr.constant.Endpoint;
import by.jrr.constant.LinkGenerator;
import by.jrr.crm.common.HistoryType;
import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.mapper.SubscriptionMapper;
import by.jrr.profile.model.SubscriptionDto;
import by.jrr.profile.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SubscriptionController {

    @Autowired
    SubscriptionService subscriptionService;



    @PostMapping(Endpoint.SUBSCRIPTIONS_REQUEST)
    public ModelAndView requestSubscription(SubscriptionDto.Request subscReq, HttpServletRequest request) {
        request.getSession().setAttribute("notification", subscriptionService.requestSubscription(subscReq));
        return redirectToProfileCard(subscReq, request);
    }

    @PostMapping(Endpoint.SUBSCRIPTIONS_APPROVE)
    public ModelAndView approveSubscription(SubscriptionDto.Request subscReq, HttpServletRequest request) {
        request.getSession().setAttribute("notification", subscriptionService.approveSubscription(subscReq));
        return redirectToProfileCard(subscReq, request);

    }

    @PostMapping(Endpoint.SUBSCRIPTIONS_REJECT)
    public ModelAndView  rejectSubscription(SubscriptionDto.Request subscReq, HttpServletRequest request) {
        request.getSession().setAttribute("notification", subscriptionService.rejectSubscription(subscReq));
        return redirectToProfileCard(subscReq, request);
    }

    @PostMapping(Endpoint.SUBSCRIPTIONS_UNSUBSCRIBE)
    public ModelAndView  unsubscribe(SubscriptionDto.Request subscReq, HttpServletRequest request) {
        request.getSession().setAttribute("notification", subscriptionService.unsubscribe(subscReq));
        return redirectToProfileCard(subscReq, request);
    }

    public ModelAndView redirectToProfileCard(SubscriptionDto.Request subscReq, HttpServletRequest request) {
        if(MyHeaders.cameFrom(request).contains(Endpoint.CRM)) {
            return new ModelAndView(String.format("redirect:%s/%s", Endpoint.PROFILE_CARD_ADMIN_VIEW, subscReq.getStreamTeamProfileId()));
        }
        return new ModelAndView(String.format("redirect:%s/%s", Endpoint.PROFILE_CARD, subscReq.getStreamTeamProfileId()));
    }

}
