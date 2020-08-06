package by.jrr.moodle.service;

import by.jrr.feedback.bean.EntityType;
import by.jrr.moodle.bean.Course;
import by.jrr.moodle.bean.Topic;
import by.jrr.moodle.repository.CourseRepository;
import by.jrr.moodle.repository.TopicRepository;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfilePossessesService;
import by.jrr.profile.service.ProfileService;
import by.jrr.profile.service.StreamAndTeamSubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;
    @Autowired
    ProfilePossessesService pss;
    @Autowired
    StreamAndTeamSubscriberService streamAndTeamSubscriberService;
    @Autowired
    ProfileService profileService;

    public Page<Course> findAll(String page, String items) {
        Page<Course> topics;
        try {
            topics = courseRepository.findAll(PageRequest.of(Integer.valueOf(page), Integer.valueOf(items)));
        } catch (Exception ex) {
            topics = courseRepository.findAll(PageRequest.of(Integer.valueOf(0), Integer.valueOf(10)));
        }
        return topics;
    }

    public Course create(Course topic) {
        topic = courseRepository.save(topic);
        pss.savePossessForCurrentUser(topic.getId(), EntityType.COURSE);
        return topic;
    }
    public Course update(Course topic) {
        topic = courseRepository.save(topic);
        pss.savePossessForCurrentUser(topic.getId(), EntityType.COURSE);
        return topic;
    }
    public void delete() {
    }
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public List<Profile> findTeamsForCourseByCourseIdFomNowAndLastMonth(Long courseId) {
        List<Profile> stream = profileService.findStreamsByCourseIdFromNowAndLastMonth(courseId);
        return stream;
    }
    public List<Profile> findTeamsForCourseByCourseIdWhereEnrollIsOpen(Long courseId) {
        List<Profile> stream = profileService.findStreamsByCourseIdWhereEnrollIsOpen(courseId);
        return stream;
    }





}
