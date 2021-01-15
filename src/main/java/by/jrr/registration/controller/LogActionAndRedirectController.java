package by.jrr.registration.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.registration.bean.EventType;
import by.jrr.registration.bean.RedirectionLink;
import by.jrr.registration.bean.RedirectionLinkStatus;
import by.jrr.registration.bean.StudentActionToLog;
import by.jrr.registration.mapper.RedirectionLinkMapper;
import by.jrr.registration.service.RedirectionLinkService;
import by.jrr.registration.service.StudentActionToLogService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//import by.jrr.registration.service.RedirectionLinkService;

@Controller
public class LogActionAndRedirectController {

    @Autowired
    StudentActionToLogService satls;

    @Autowired
    UserDataToModelService userDataToModelService;

    @Autowired
    RedirectionLinkService redirectionLinkService;

    @GetMapping(Endpoint.REDIRECT+"/{redirectionId}")
    public ModelAndView openRedirect(@PathVariable String redirectionId, HttpServletRequest httpServletRequest) {

        ModelAndView mov = new ModelAndView();
        mov.setViewName(View.PAGE_304);

        //filter Viber requests
        if (!isRobotRequest(httpServletRequest)) {

            RedirectionLink redirectionLink = redirectionLinkService.useRedirectionLink(redirectionId);
            if(redirectionLink.getStatus().equals(RedirectionLinkStatus.NEW)) {
                satls.saveAction(RedirectionLinkMapper.OF.getStudentActionToLogFromRedirectionLink(redirectionLink));
            }
            mov.addObject("link", redirectionLink);
        }
//        https://stackoverflow.com/questions/5411538/redirect-from-an-html-page
        return mov;
    }

    private boolean isRobotRequest(HttpServletRequest request) {
        return request.getHeader("User-Agent").contains("Viber")
                || request.getHeader("User-Agent").contains("TelegramBotможе");
    }

    @PostMapping("/l/") // TODO: 24/06/20 add to endpoint mapping
    public RedirectView logAndRedirectFromPost(@RequestParam Long streamTeamId,
                                               @RequestParam String link,
                                               @RequestParam String linkName,
                                               @RequestParam EventType eventType,
                                               @RequestParam(value = "courseId", required = false) Long courseId,
                                               @RequestParam(value = "lectureId", required = false) Long lectureId,
                                               @RequestParam(value = "timelineUUID", required = false) String timelineUUID
    ) {
        satls.saveAction(StudentActionToLog.builder()
                .streamTeamProfileId(streamTeamId)
                .urlToRedirect(link)
                .eventName(linkName)
                .eventType(eventType)
                .courseId(courseId)
                .lectureId(lectureId)
                .timelineUUID(timelineUUID)
                .build());
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(link);
        return redirectView;
    }

    @Deprecated
    @GetMapping("/l/{link}/{streamTeamId}/{eventType}")
    public RedirectView logAndRedirectByGet(@PathVariable Long streamTeamId,
                                            @PathVariable String link,
                                            @PathVariable String linkName,
                                            @PathVariable String eventType) { // TODO: 24/06/20 try to use enum here
        satls.saveAction(streamTeamId, eventType, link, linkName);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(link);
        return redirectView;
    }

    @Deprecated
    @GetMapping(value = "/admin/action", produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    UserActivityDTO findStudentWhoTakeNoAction(@RequestParam Optional<Long> streamId,
                                               @RequestParam Optional<Long> userProfileId,
                                               @RequestParam Optional<String> from,
                                               @RequestParam Optional<String> to) {
        UserActivityDTO userActivityDTO = new UserActivityDTO();
        // search active users for team / stream
        if (streamId.isPresent() && from.isPresent() && to.isPresent()) {
            try {
                LocalDateTime startDate = LocalDateTime.parse(from.get());
                LocalDateTime endDate = LocalDateTime.parse(to.get());
                userActivityDTO =
                        satls.findActionForStreamBetweenTimestamps(streamId.get(), startDate, endDate);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return userActivityDTO;
    }

    @Deprecated
    @GetMapping(value = "/admin/userActivity", produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    UserActivityDTO findUserActivity(@RequestParam Optional<Long> userProfileId,
                                     @RequestParam Optional<String> from,
                                     @RequestParam Optional<String> to) {
        UserActivityDTO userActivityDTO = new UserActivityDTO();

        if (userProfileId.isPresent() && from.isPresent() && to.isPresent() && !from.get().isEmpty() && !to.get().isEmpty()) {
            try {
                LocalDateTime startDate = LocalDateTime.parse(from.get());
                LocalDateTime endDate = LocalDateTime.parse(to.get());
                userActivityDTO.setUserActivity(satls.convertUserActivityListToDTO(
                        satls.findAllActionsForProfileIdBetwenDates(userProfileId.get(), startDate, endDate)));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (userProfileId.isPresent()) {
            try {
                userActivityDTO.setUserActivity(satls.convertUserActivityListToDTO(
                        satls.findAllActionsForProfileId(userProfileId.get())));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return userActivityDTO;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @XmlRootElement
    @Deprecated
    public static class UserActivityDTO {
        List<UserActivityElementDTO> active = new ArrayList<>();
        List<UserActivityElementDTO> notActive = new ArrayList<>();
        List<UserActivityElementDTO> userActivity = new ArrayList<>();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @XmlRootElement
    @Deprecated
    public static class UserActivityElementDTO {
        String firstName;
        String lastName;
        String email;
        String userName;
        String phone;
        String eventType;
        String eventName;
        String timestamp;
        String profileLink;
    }
}
