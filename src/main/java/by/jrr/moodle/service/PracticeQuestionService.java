package by.jrr.moodle.service;

import by.jrr.auth.service.UserService;
import by.jrr.feedback.bean.EntityType;
import by.jrr.moodle.bean.PracticeQuestion;
import by.jrr.moodle.repository.PracticeQuestionRepository;
import by.jrr.profile.service.ProfilePossessesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PracticeQuestionService {

    @Autowired
    PracticeQuestionRepository practiceServiceRepository;
    @Autowired
    UserService userService;
    @Autowired
    ProfilePossessesService pss;


    public Page<PracticeQuestion> findAll(String page, String items) {
        Page<PracticeQuestion> topics;
        try {
            topics = practiceServiceRepository.findAll(PageRequest.of(Integer.valueOf(page), Integer.valueOf(items)));
        } catch (Exception ex) {
            topics = practiceServiceRepository.findAll(PageRequest.of(Integer.valueOf(0), Integer.valueOf(10)));
        }
        return topics;
    }

    public PracticeQuestion create(PracticeQuestion topic) {
        topic = practiceServiceRepository.save(topic);
        pss.savePossessForCurrentUser(topic.getId(), EntityType.PRACTICE_QUESTION);
        return topic;
    }
    public PracticeQuestion update(PracticeQuestion topic) {
        topic = practiceServiceRepository.save(topic);
        pss.savePossessForCurrentUser(topic.getId(), EntityType.PRACTICE_QUESTION);
        return topic;
    }
    public void delete() {
    }
    public Optional<PracticeQuestion> findById(Long id) {
        return practiceServiceRepository.findById(id);
    }


}
