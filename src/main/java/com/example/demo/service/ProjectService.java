package com.example.demo.service;

import com.example.demo.entity.Project;
import com.example.demo.model.CreateProjectRequest;
import com.example.demo.model.ProjectResponse;
import com.example.demo.model.UpdateProjectRequest;
import com.example.demo.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request) {
        validationService.validate(request);

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());

        projectRepository.save(project);

        return toResponse(project);
    }

    private ProjectResponse toResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .build();
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProject(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "project not found"));

        return toResponse(project);
    }

    @Transactional
    public ProjectResponse updateProject(String id, UpdateProjectRequest request) {
        validationService.validate(request);

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        project.setName(request.getName());
        project.setDescription(request.getDescription());


        projectRepository.save(project);

        return toResponse(project);
    }

    @Transactional
    public void deleteProject(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        projectRepository.delete(project);
    }
}
