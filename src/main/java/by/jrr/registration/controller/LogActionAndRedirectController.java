package by.jrr.registration.controller;

import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import by.jrr.registration.bean.StudentActionToLog;
import by.jrr.registration.service.StudentActionToLogService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class LogActionAndRedirectController {

    @Autowired
    StudentActionToLogService satls;

    @PostMapping("/l/") // TODO: 24/06/20 add to endpoint mapping
    public RedirectView logAndRedirectFromPost(@RequestParam Long streamTeamId,
                                               @RequestParam String link,
                                               @RequestParam String linkName,
                                               @RequestParam String eventType) { // TODO: 24/06/20 try to use enum here
        satls.saveAction(streamTeamId, eventType, link, linkName);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(link);
        return redirectView;
    }

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
    public static class UserActivityDTO {
        List<UserActivityElementDTO> active = new ArrayList<>();
        List<UserActivityElementDTO> notActive = new ArrayList<>();
        List<UserActivityElementDTO> userActivity = new ArrayList<>();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @XmlRootElement
    public static class UserActivityElementDTO {
        String firstName;
        String lastName;
        String email;
        String userName;
        String phone;
        String eventType;
        String eventName;
        String timestamp;
    }
}
