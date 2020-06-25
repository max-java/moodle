package by.jrr.registration.controller;

import by.jrr.registration.service.StudentActionToLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LogActionAndRedirectController {

    @Autowired
    StudentActionToLogService satls;

    @PostMapping("/l/") // TODO: 24/06/20 add to endpoint mapping
    public RedirectView logAndRedirectFromPost(@RequestParam Long streamTeamId,
                               @RequestParam String link,
                               @RequestParam String eventType) { // TODO: 24/06/20 try to use enum here
        satls.saveAction(streamTeamId, eventType, link);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(link);
        return redirectView;
    }

    @GetMapping("/l/{link}/{streamTeamId}/{eventType}")
    public RedirectView logAndRedirectByGet(@PathVariable Long streamTeamId,
                               @PathVariable String link,
                               @PathVariable String eventType) { // TODO: 24/06/20 try to use enum here
        satls.saveAction(streamTeamId, eventType, link);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(link);
        return redirectView;
    }
}
