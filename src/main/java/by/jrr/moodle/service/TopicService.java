package by.jrr.moodle.service;

import by.jrr.moodle.bean.Topic;
import by.jrr.moodle.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopicService {

    @Autowired
    TopicRepository topicRepository;

    public Page<Topic> findAll(String page, String items) {
        Page<Topic> topics;
        try {
            topics = topicRepository.findAll(PageRequest.of(Integer.valueOf(page), Integer.valueOf(items)));
        } catch (Exception ex) {
            topics = topicRepository.findAll(PageRequest.of(Integer.valueOf(0), Integer.valueOf(10)));
        }
        return topics;
    }

    public Topic create(Topic topic) {
        topic = topicRepository.save(topic);
        return topic;
    }
    public Topic update(Topic topic) {
        topic = topicRepository.save(topic);
        return topic;
    }
    public void delete() {
    }
    public Optional<Topic> findById(Long id) {
        return topicRepository.findById(id);
    }




}
