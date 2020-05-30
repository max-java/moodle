package by.jrr.moodle.service;

import by.jrr.moodle.bean.Lecture;
import by.jrr.moodle.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LectureService {

    @Autowired
    LectureRepository lectureRepository;

    public Page<Lecture> findAll(String page, String items) {
        Page<Lecture> topics;
        try {
            topics = lectureRepository.findAll(PageRequest.of(Integer.valueOf(page), Integer.valueOf(items)));
        } catch (Exception ex) {
            topics = lectureRepository.findAll(PageRequest.of(Integer.valueOf(0), Integer.valueOf(10)));
        }
        return topics;
    }

    public Lecture create(Lecture topic) {
        topic = lectureRepository.save(topic);
        return topic;
    }
    public Lecture update(Lecture topic) {
        topic = lectureRepository.save(topic);
        return topic;
    }
    public void delete() {
    }
    public Optional<Lecture> findById(Long id) {
        return lectureRepository.findById(id);
    }




}
