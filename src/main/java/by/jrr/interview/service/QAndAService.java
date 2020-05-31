package by.jrr.interview.service;

import by.jrr.interview.bean.QAndA;
import by.jrr.interview.repository.QAndARepository;
import by.jrr.moodle.bean.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QAndAService {

    @Autowired
    QAndARepository qAndARepository;

    public Page<QAndA> findAll(String page, String items) {
        Page<QAndA> qAndAPage;
        try {
            qAndAPage = qAndARepository.findAll(PageRequest.of(Integer.valueOf(page), Integer.valueOf(items)));
        } catch (Exception ex) {
            qAndAPage = qAndARepository.findAll(PageRequest.of(Integer.valueOf(0), Integer.valueOf(500)));
        }
        return qAndAPage;
    }

    public QAndA create(QAndA qAndA) {
        qAndA = qAndARepository.save(qAndA);
        return qAndA;
    }
    public QAndA update(QAndA qAndA) {
        qAndA = qAndARepository.save(qAndA);
        return qAndA;
    }
    public void delete() {
    }
    public Optional<QAndA> findById(Long id) {
        return qAndARepository.findById(id);
    }
    public void saveAll(List<QAndA> topicList) {
        qAndARepository.saveAll(topicList);
    }




}
