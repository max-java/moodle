package by.jrr.portfolio.service;

import by.jrr.feedback.bean.EntityType;
import by.jrr.portfolio.bean.Domain;
import by.jrr.portfolio.repository.DomainRepository;
import by.jrr.profile.service.ProfilePossessesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DomainService {

    @Autowired
    DomainRepository domainRepository;
    @Autowired
    ProfilePossessesService pss;

    public Page<Domain> findAll(String page, String items) {
        Page<Domain> projectPage;
        try {
            projectPage = domainRepository.findAll(PageRequest.of(Integer.valueOf(page), Integer.valueOf(items)));
        } catch (Exception ex) {
            projectPage = domainRepository.findAll(PageRequest.of(Integer.valueOf(0), Integer.valueOf(10)));
        }
        return projectPage;
    }

    public Domain create(Domain domain) {
        domain = domainRepository.save(domain);
        pss.savePossessForCurrentUser(domain.getId(), EntityType.DOMAIN);
        return domain;
    }
    public Domain update(Domain domain) {
        domain = domainRepository.save(domain);
        pss.savePossessForCurrentUser(domain.getId(), EntityType.DOMAIN);
        return domain;
    }
    public void delete() {
    }
    public Optional<Domain> findById(Long id) {
        return domainRepository.findById(id);
    }




}
