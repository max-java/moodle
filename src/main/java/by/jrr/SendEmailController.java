//package by.jrr;
//
//import by.jrr.auth.service.UserService;
//import by.jrr.email.service.EMailService;
//import by.jrr.moodle.bean.Lecture;
//import by.jrr.moodle.service.LectureService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Controller
//public class SendEmailController {
//
//    @Autowired
//    LectureService lectureService;
//    @Autowired
//    EMailService eMailService;
//    @Autowired
//    UserService userService;
//
//    @GetMapping("/sendEmail")
//    public ModelAndView sendEmail() {
//
//        Lecture lecture = lectureService.findById(9641L).get();
////        List<String> emails = Arrays.asList("6666350@gmail.com", "info@jrr.by");
//        List<String> emails = userService.findAllUsers().stream()
//                .map(a-> a.getEmail())
//                .distinct()
//                .collect(Collectors.toList());
//        int totalLeft = emails.size();
//        LocalDateTime start = LocalDateTime.now();
//        for(String email : emails) {
//            if(totalLeft > 63) {
//                --totalLeft;
//            } else {
//                eMailService.sendLecture1GoToGoogleEmail(email, lecture);
//                System.out.println("totalLeft = " + --totalLeft);
//                LocalDateTime now = LocalDateTime.now();
//                Duration duration = Duration.between(start, now);
//                System.out.println("duration.getSeconds() = " + duration.getSeconds());
//            }
//
//        }
//
//
//
//        return new ModelAndView("redirect:/");
//    }
//}
