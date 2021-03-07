package by.jrr.registration.controller;

import by.jrr.auth.configuration.annotations.AccessAdminAndSales;
import by.jrr.auth.configuration.annotations.AdminOnly;
import by.jrr.constant.Endpoint;
import by.jrr.registration.bean.RedirectionLink;
import by.jrr.registration.model.RedirectionLinkDto;
import by.jrr.registration.service.RedirectionLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RedirectionLinkApiController {

    @Autowired
    RedirectionLinkService redirectionLinkService;

    @AccessAdminAndSales
    @PostMapping(Endpoint.API_CREATE_REDIRECTION_LINK)
    public RedirectionLinkDto.Response createRedirectionLinkToExternalResource(@RequestBody RedirectionLinkDto.Request request) {
        return redirectionLinkService.createRedirectionLink(request);
    }

    @AccessAdminAndSales
    @PostMapping(Endpoint.API_REDIRECTION_LINKS)
    public List<RedirectionLink> getRedirectionLinkList(@RequestBody RedirectionLinkDto.Request request) {
//        if(request.getStreamTeamProfileId() != null) {
//            return redirectionLinkService.findRedirectionLinksForStreamByStreamId(request.getStreamTeamProfileId());
//        } else {
//            return redirectionLinkService.findRedirectionLinksForUserByProfileId(request.getStudentProfileId());
//        }
        //todo: replace with criteria API
        return null;
    }
}
