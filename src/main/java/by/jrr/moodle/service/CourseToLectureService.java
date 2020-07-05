package by.jrr.moodle.service;

import by.jrr.moodle.bean.Course;
import by.jrr.moodle.bean.CourseToLecture;
import by.jrr.moodle.bean.Lecture;
import by.jrr.moodle.repository.CourseToLectureRepository;
import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseToLectureService {
    @Autowired
    CourseToLectureRepository courseToLectureRepository;
    @Autowired
    LectureService lectureService;

    public void saveAll(List<CourseToLecture> courseToLectureList) {
        courseToLectureRepository.saveAll(courseToLectureList);
    }

    public List<CourseToLecture> findCourseToLectureByCourseId(Long id) {
        return courseToLectureRepository.findAllByCourseId(id);
    }
    public List<Lecture> findLecturesForCourse(@Nullable Long id, @Nullable Course course) {
        List<Lecture> lectures = new ArrayList<>();
        if (course != null) {
            id = course.getId();
        }
        if (id != null) {
            List<CourseToLecture> courseToLectures = this.findCourseToLectureByCourseId(id);
            courseToLectures.forEach(ctl -> lectures.add(lectureService.findById(ctl.getLectureId()).orElseGet(Lecture::new)));
            // TODO: 24/06/20 if it is empty lecture, than lecture for course has been deleted, but not deleted from courde. Should be logged and deleted from course.
        }
        // TODO: 05/07/20 investigate, why for https://moodle.jrr.by/profile/230 and user  https://moodle.jrr.by/profile/22 Lectures are duplicated?
        return lectures.stream()
                .distinct()
                .collect(Collectors.toList()); // TODO: 05/07/20 replace this HotFix
        
    }
    public List<Long> findLecturesIdForCourse(@Nullable Long id, @Nullable Course course) {
        List<Long> lecturesId = new ArrayList<>();
        if (course != null) {
            id = course.getId();
        }
        if (id != null) {
            List<CourseToLecture> courseToLectures = this.findCourseToLectureByCourseId(id);
            courseToLectures.forEach(ctl -> lecturesId.add(ctl.getLectureId()));
        }
        return lecturesId;
    }

}
