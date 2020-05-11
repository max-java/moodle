package by.jrr.project.service;

import by.jrr.project.bean.Project;
import by.jrr.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    public Page<Project> findAll(String page, String items) {
        Page<Project> projectPage;
        try {
            projectPage = projectRepository.findAll(PageRequest.of(Integer.valueOf(page), Integer.valueOf(items)));
        } catch (Exception ex) {
            projectPage = projectRepository.findAll(PageRequest.of(Integer.valueOf(0), Integer.valueOf(10)));
        }
        return projectPage;
    }

    public Project create(Project project) {
        project = projectRepository.save(project);
        return project;
    }
    public Project update(Project project) {
        project = projectRepository.save(project);
        return project;
    }
    public void delete() {
    }
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }




}
