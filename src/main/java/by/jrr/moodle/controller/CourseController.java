package by.jrr.moodle.controller;

import by.jrr.auth.bean.User;
import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.configuration.annotations.AdminOnly;
import by.jrr.auth.service.UserAccessService;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.common.bean.UrchinTracking;
import by.jrr.common.repository.UrchinTrackingRepository;
import by.jrr.common.service.UtmService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.moodle.bean.Course;
import by.jrr.moodle.bean.CourseToLecture;
import by.jrr.moodle.service.CourseService;
import by.jrr.moodle.service.CourseToLectureService;
import by.jrr.moodle.service.LectureService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.service.ProfilePossessesService;
import by.jrr.profile.service.ProfileService;
import by.jrr.profile.service.StreamAndTeamSubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class CourseController { // TODO: 30/05/20  make it like in userProfile & qAndA Trackable

    @Autowired
    CourseService courseService;
    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    StreamAndTeamSubscriberService streamAndTeamSubscriberService;
    @Autowired
    ProfileService profileService;
    @Autowired
    ProfilePossessesService pss;
    @Autowired
    LectureService lectureService;
    @Autowired
    CourseToLectureService courseToLectureService;
    @Autowired
    UrchinTrackingRepository urchinTrackingRepository;
    @Autowired
    UtmService utmService;

    @AdminOnly
    @GetMapping(Endpoint.COURSE)
    public ModelAndView createNewTopic() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("topic", new Course());
        mov.addObject("edit", true);
        mov.addObject(true);


        mov.setViewName(View.COURSE);
        return mov;
    }


    @GetMapping(Endpoint.COURSE + "/{id}")
    public ModelAndView openTopicById(@PathVariable Long id, @RequestParam Map<String,String> allParams) {
        utmService.setTrack(allParams);

        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<Course> topic = courseService.findById(id);
        if (topic.isPresent()) {
            mov.addObject("topic", topic.get());
            mov.addObject("user", new User()); // TODO: 10/06/20 is it really need?
            mov.setViewName(View.COURSE);
        } else {
            mov.setStatus(HttpStatus.NOT_FOUND);
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }

    @AdminOnly
    @PostMapping(Endpoint.COURSE)
    public ModelAndView saveNewTopic(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "subtitle", required = false) String subtitle,
            @RequestParam(value = "text", required = false) String text
    ) {
        Course topic = courseService.create(Course.builder()
                .title(title)
                .subtitle(subtitle)
                .text(text)
                .build());
        return new ModelAndView("redirect:" + Endpoint.COURSE + "/" + topic.getId());
    }

    @PostMapping(Endpoint.COURSE + "/{id}")
    public ModelAndView saveNewTopic(@PathVariable Long id,
                                     @RequestParam(value = "title", required = false) String title,
                                     @RequestParam(value = "subtitle", required = false) String subtitle,
                                     @RequestParam(value = "text", required = false) String text,
                                     @RequestParam(value = "edit", required = false) boolean edit,
                                     @RequestParam Optional<String> subscribe,
                                     @RequestParam Optional<Long[]> lecturesId
    ) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.COURSE);
        Optional<Course> topic = courseService.findById(id);
        mov.addObject("topic", topic.orElseGet(Course::new));
        if (subscribe.isPresent()) { // TODO: 10/06/20 move here register and subscribe!!!!
            return enrollCurentUser(id);
        } else if (edit && UserAccessService.hasRole(UserRoles.ROLE_ADMIN)) { // TODO: 24/06/20 encapsulate it to "openForEdit"
            if (topic.isPresent()) {
                mov.addObject("topic", topic.get());
                mov.addObject("edit", true);
                mov.addObject("lectures", lectureService.findAll());
                mov.addObject("courseLectures", courseToLectureService.findLecturesIdForCourse(id, null));
            } else { // TODO: 11/05/20 impossible situation, but should be logged
                mov.setViewName(View.PAGE_404);
            }
        } else if (UserAccessService.hasRole(UserRoles.ROLE_ADMIN)) { // TODO: 24/06/20 encapsulate it to "save Edited"
            Course courseToUpdate = Course.builder()
                    .title(title)
                    .subtitle(subtitle)
                    .text(text)
                    .Id(id)
                    .build();
            Course updatedCourse = courseService.update(courseToUpdate);
            if(lecturesId.isPresent()) {
                List<CourseToLecture> courseToLectureList = new ArrayList<>();
                for (Long lectureId: lecturesId.get()) {
                    courseToLectureList.add(CourseToLecture.builder().courseId(updatedCourse.getId()).lectureId(lectureId).build());
                    courseToLectureService.saveAll(courseToLectureList);
                }
            }

            mov.addObject("topic", updatedCourse);
            mov.addObject("edit", false);
            return new ModelAndView("redirect:" + Endpoint.COURSE + "/" + id);
        }
        return mov;
        // TODO: 11/05/20 replace if-else with private methods
    }

    private ModelAndView enrollCurentUser(Long courseId) {
        Optional<Profile> stream = streamAndTeamSubscriberService.findStreamForCourse(courseId);
        if (stream.isPresent()) {
            streamAndTeamSubscriberService.updateSubscription(stream.get().getId(), profileService.getCurrentUserProfile().getId(), SubscriptionStatus.REQUESTED);
            return new ModelAndView("redirect:" + Endpoint.PROFILE_CARD + "/" + stream.get().getId());
        } else {
            // TODO: 10/06/20 handle exception no open courses
            return new ModelAndView("redirect:" + Endpoint.COURSE + "/" + courseId);
        }
    }

    @GetMapping(Endpoint.COURSE_LIST)
    public ModelAndView findAll(@PathVariable(required = false) String page, @PathVariable(required = false) String size) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Page<Course> topicList = courseService.findAll(page, size);
        mov.addObject("topicList", topicList);
        mov.setViewName(View.COURSE_LIST);
        return mov;
    }
}
