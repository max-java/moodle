package by.jrr.registration.controller;

import by.jrr.auth.configuration.annotations.AdminOnly;
import by.jrr.constant.Endpoint;
import by.jrr.registration.model.CreateRedirectionLink;
import by.jrr.registration.service.RedirectionLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectionLinkApiController {

    @Autowired
    RedirectionLinkService redirectionLinkService;

    @AdminOnly
    @PostMapping(Endpoint.API_CREATE_REDIRECTION_LINK)
    public CreateRedirectionLink.Response createRedirectionLinkToExternalResource(@RequestBody CreateRedirectionLink.Request request) {
        return redirectionLinkService.createRedirectionLink(request);
    }
}
