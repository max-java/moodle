package by.jrr.portfolio.controller;

import by.jrr.auth.configuration.annotations.AdminOnly;
import by.jrr.auth.configuration.annotations.AtLeatStudent;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.portfolio.bean.Domain;
import by.jrr.portfolio.service.SubjectService;
import by.jrr.portfolio.service.DomainService;
import by.jrr.profile.service.ProfilePossessesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class DomainController {

    @Autowired
    DomainService domainService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    ProfilePossessesService pss;

    @AdminOnly
    @GetMapping(Endpoint.DOMAIN)
    public ModelAndView createNewDomain() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("domain", new Domain());
        mov.addObject("edit", true);
        mov.setViewName(View.DOMAIN);
        return mov;
    }

    @GetMapping(Endpoint.DOMAIN + "/{id}")
    public ModelAndView openDomainById(@PathVariable Long id) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<Domain> domain = domainService.findById(id);
        if (domain.isPresent()) {
            mov.addObject("domain", domain.get());
            mov.addObject("subjectList", subjectService.findAllByDomainId(domain.get().getId()));
            mov.setViewName(View.DOMAIN);
        } else {
            mov.setStatus(HttpStatus.NOT_FOUND);
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }

    @AdminOnly
    @PostMapping(Endpoint.DOMAIN)
    public ModelAndView saveNewDomain(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "description") String description
    ) {
        Domain domain = domainService.create(Domain.builder()
                .name(name)
                .description(description)
                .build());
        return new ModelAndView("redirect:" + Endpoint.DOMAIN + "/" + domain.getId());
    }

    @AdminOnly
    @PostMapping(Endpoint.DOMAIN + "/{id}")
    public ModelAndView updateDomain(@PathVariable Long id,
                                     @RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "description", required = false) String description,
                                     @RequestParam(value = "edit", required = false) boolean edit
                                    ) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.DOMAIN);
        if (edit) {
            Optional<Domain> domain = domainService.findById(id);
            if (domain.isPresent()) {
                mov.addObject("domain", domain.get());
                mov.addObject("edit", true);
            } else { // TODO: 11/05/20 impossible situation, but should be logged
                mov.setViewName(View.PAGE_404);
            }
        } else {
            Domain domain = domainService.update(Domain.builder()
                    .name(name)
                    .description(description)
                    .Id(id)
                    .build());
            mov.addObject("domain", domain);
            mov.addObject("edit", false);
        }
        return mov;
        // TODO: 11/05/20 replace if-else with private methods
    }

    @GetMapping(Endpoint.DOMAIN_LIST)
    public ModelAndView findAll(@PathVariable(required = false) String page, @PathVariable(required = false) String size) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Page<Domain> domainPage = domainService.findAll(page, size);
        mov.addObject("domainPage", domainPage);
        mov.setViewName(View.DOMAIN_LIST);
        return mov;
    }
}
