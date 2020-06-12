package by.jrr.moodle.repository;

import by.jrr.moodle.bean.Lecture;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends PagingAndSortingRepository<Lecture, Long> {
}
