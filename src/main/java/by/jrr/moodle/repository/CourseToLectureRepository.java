package by.jrr.moodle.repository;

import by.jrr.moodle.bean.CourseToLecture;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Repository
public interface CourseToLectureRepository extends PagingAndSortingRepository<CourseToLecture, Long> {
    List<CourseToLecture> findAllByCourseId(Long id);
}
