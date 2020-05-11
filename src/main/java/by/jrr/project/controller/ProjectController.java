package by.jrr.project.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.project.bean.Project;
import by.jrr.project.service.ProjectService;
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
public class ProjectController {

    @Autowired
    ProjectService projectService;
    @Autowired
    UserDataToModelService userDataToModelService;

    @GetMapping(Endpoint.PROJECT)
    public ModelAndView createNewProject() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("project", new Project());
        mov.addObject("edit", true);
        mov.setViewName(View.PROJECT);
        return mov;
    }

    @GetMapping(Endpoint.PROJECT + "/{id}")
    public ModelAndView openQAndAById(@PathVariable Long id) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<Project> project = projectService.findById(id);
        if (project.isPresent()) {
            mov.addObject("project", project.get());
            mov.setViewName(View.PROJECT);
        } else {
            mov.setStatus(HttpStatus.NOT_FOUND);
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }

    @PostMapping(Endpoint.PROJECT)
    public ModelAndView saveNewProject(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description
    ) {
        Project project = projectService.create(Project.builder()
                .name(name)
                .description(description)
                .build());
        return new ModelAndView("redirect:" + Endpoint.PROJECT + "/" + project.getId());
    }

    @PostMapping(Endpoint.PROJECT + "/{id}")
    public ModelAndView updateProject(@PathVariable Long id,
                                     @RequestParam(value = "question", required = false) String name,
                                     @RequestParam(value = "answer", required = false) String description,
                                     @RequestParam(value = "edit", required = false) boolean edit
                                    ) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.PROJECT);
        if (edit) {
            Optional<Project> project = projectService.findById(id);
            if (project.isPresent()) {
                mov.addObject("project", project.get());
                mov.addObject("edit", true);
            } else { // TODO: 11/05/20 impossible situation, but should be logged
                mov.setViewName(View.PAGE_404);
            }
        } else {
            Project project = projectService.update(Project.builder()
                    .name(name)
                    .description(description)
                    .Id(id)
                    .build());
            mov.addObject("project", project);
            mov.addObject("edit", false);
        }
        return mov;
        // TODO: 11/05/20 replace if-else with private methods
    }

    @GetMapping(Endpoint.PROJECT_LIST)
    public ModelAndView findAll(@PathVariable(required = false) String page, @PathVariable(required = false) String size) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Page<Project> projectPage = projectService.findAll(page, size);
        mov.addObject("projectPage", projectPage);
        mov.setViewName(View.PROJECT_LIST);
        return mov;
    }
}
