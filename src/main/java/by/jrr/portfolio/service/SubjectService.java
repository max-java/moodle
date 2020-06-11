package by.jrr.portfolio.service;

import by.jrr.auth.bean.User;
import by.jrr.auth.service.UserService;
import by.jrr.portfolio.bean.Domain;
import by.jrr.portfolio.bean.Subject;
import by.jrr.portfolio.repository.SubjectRepository;
import by.jrr.profile.service.ProfilePossessesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    UserService userService;
    @Autowired
    DomainService domainService;
    @Autowired
    ProfilePossessesService pss;

    public Page<Subject> findAll(String page, String items) {
        Page<Subject> subjectPage;
        try {
            subjectPage = subjectRepository.findByLastInHistory(true, PageRequest.of(Integer.valueOf(page), Integer.valueOf(items)));
            subjectPage.forEach(this::setUsersAndDomainToSubject);
        } catch (Exception ex) {
            subjectPage = subjectRepository.findByLastInHistory(true, PageRequest.of(Integer.valueOf(0), Integer.valueOf(10)));
            subjectPage.forEach(this::setUsersAndDomainToSubject);
        }
        return subjectPage;
    }
    public Page<Subject> findAllByDomainIdPageable(Long domainId, String page, String items) {
        Page<Subject> subjectPage;
        try {
            subjectPage = subjectRepository.findByDomainIdAndLastInHistory(domainId,true, PageRequest.of(Integer.valueOf(page), Integer.valueOf(items)));
            subjectPage.forEach(this::setUsersAndDomainToSubject);
        } catch (Exception ex) {
            subjectPage = subjectRepository.findByDomainIdAndLastInHistory(domainId, true, PageRequest.of(Integer.valueOf(0), Integer.valueOf(10)));
            subjectPage.forEach(this::setUsersAndDomainToSubject);
        }
        return subjectPage;
    }
    public List<Subject> findAllByDomainId(Long domainId) {
        List<Subject> subjectPage = new ArrayList<>();
        try {
            subjectPage = subjectRepository.findByDomainIdAndLastInHistory(domainId,true);
            subjectPage.forEach(this::setUsersAndDomainToSubject);
        } catch (Exception ex) {
            subjectPage = subjectRepository.findByDomainIdAndLastInHistory(domainId, true);
            subjectPage.forEach(this::setUsersAndDomainToSubject);
        }
        return subjectPage;
    }


    public Subject createOrUpdate(Subject subject) {
        //subject could not be updated, only new record with new data storied to enable history
        //find last history record for this subject and set that it is not the last
        if (subject.getSubjectId() != null) {
            Optional<Subject> lastRecordOpt = this.findBySubjectId(subject.getSubjectId());
            if (lastRecordOpt.isPresent()) {
                Subject lastRecord = lastRecordOpt.get();
                lastRecord.setLastInHistory(false);
                subjectRepository.save(lastRecord);
            }
        }
        subject.setId(null); //subject could not be updated, only new record with new data storied to enable history
        subject.setTimeStamp(LocalDateTime.now()); // history enabled by setting timestamp
        subject.setLastInHistory(true); // set that it is last record in history
        subject = subjectRepository.save(subject);
        if (subject.getSubjectId() == null) { //if it is first history record, that subject id set from id value
            subject.setSubjectId(subject.getId());
            subjectRepository.save(subject);
        }
        return subject;
    }

    public void delete() {
    }

    public Optional<Subject> findBySubjectId(Long subjectId) {
        List<Subject> subjectList = subjectRepository.findBySubjectId(subjectId); //it returns all history for subject, so I need to pick up only last one
        if (subjectList != null) {
            Optional<Subject> subject = subjectList.stream().max(Comparator.comparing(Subject::getTimeStamp));
            if (subject.isPresent()) {
                subject.get().setHistory(subjectList);
                subject = this.setUsersAndDomainToSubject(subject);
            }
            return subject;
        } else {
            return Optional.ofNullable(null);
        }
    }

    public Optional<List<Subject>> findHistoryBySubjectId(Long subjectId) {
        return Optional.ofNullable(subjectRepository.findBySubjectId(subjectId));
    }

    private Optional<Subject> setUsersAndDomainToSubject(Optional<Subject> subject) {
        if (subject.isPresent()) {
            if (subject.get().getAssigneeUserId() != null) {
                subject.get().setAssignee(userService.findUserById(subject.get().getAssigneeUserId()).orElse(new User()));
            }
            if (subject.get().getSubmitterUserId() != null) {
                subject.get().setSubmitter(userService.findUserById(subject.get().getSubmitterUserId()).orElse(new User()));
            }
            if (subject.get().getDomainId() != null) {
                subject.get().setDomain(domainService.findById(subject.get().getDomainId()).orElse(new Domain()));
            }
        }
        return subject;
    }
    private Subject setUsersAndDomainToSubject(Subject subject) {

            if (subject.getAssigneeUserId() != null) {
                subject.setAssignee(userService.findUserById(subject.getAssigneeUserId()).orElse(new User()));
            }
            if (subject.getSubmitterUserId() != null) {
                subject.setSubmitter(userService.findUserById(subject.getSubmitterUserId()).orElse(new User()));
            }
            if (subject.getDomainId() != null) {
                subject.setDomain(domainService.findById(subject.getDomainId()).orElse(new Domain()));
            }

        return subject;
    }


}
