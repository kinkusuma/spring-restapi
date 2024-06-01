package com.example.demo.service;

import com.example.demo.entity.EProjectStatus;
import com.example.demo.entity.Project;
import com.example.demo.entity.User;
import com.example.demo.model.*;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public ProjectResponse createProject(User user, CreateProjectRequest request) {
        validationService.validate(request);

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setBudget(request.getBudget());
        project.setDeadline(request.getDeadline());
        project.setVideoPresentationUrl(request.getVideoPresentationUrl());
        project.setImageUrl(request.getImageUrl());
        project.setManager(user);
        project.setMembers(List.of(user));

        projectRepository.save(project);

        return toResponse(project);
    }

    private ProjectResponse toResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .budget(project.getBudget())
                .deadline(project.getDeadline())
                .videoPresentationUrl(project.getVideoPresentationUrl())
                .imageUrl(project.getImageUrl())
                .projectManager(UserResponse.builder()
                        .id(project.getManager().getId())
                        .name(project.getManager().getName())
                        .email(project.getManager().getEmail())
                        .build())
                .members(Optional.ofNullable(project.getMembers()).orElse(Collections.emptyList()).stream()
                        .map(user -> UserResponse.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .email(user.getEmail())
                                .build())
                        .toList())
                .tasks(Optional.ofNullable(project.getTasks()).orElse(Collections.emptyList()).stream()
                        .map(task -> TaskResponse.builder()
                                .id(task.getId())
                                .title(task.getTitle())
                                .description(task.getDescription())
                                .deadline(task.getDeadline())
                                .documentUrl(task.getDocumentUrl())
                                .build())
                        .toList())
                .build();

    }

    @Transactional(readOnly = true)
    public ProjectResponse getProject(String id, User user) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "project not found"));

        if (!project.getMembers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this project");
        }

        return toResponse(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjects(User user) {
        return userRepository.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"))
                .getProjects().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProjectResponse updateProject(String id, User user, UpdateProjectRequest request) {
        validationService.validate(request);

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (!project.getManager().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this project");
        }

        request.getName().ifPresent(project::setName);
        request.getDescription().ifPresent(project::setDescription);
        request.getBudget().ifPresent(project::setBudget);
        request.getDeadline().ifPresent(project::setDeadline);
        request.getVideoPresentationUrl().ifPresent(project::setVideoPresentationUrl);
        request.getImageUrl().ifPresent(project::setImageUrl);

        projectRepository.save(project);

        return toResponse(project);
    }

    @Transactional
    public ProjectResponse reassignManager(String id, User user, ReassignProjectManagerRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (!project.getManager().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to reassign the project manager");
        }

        User newManager = project.getMembers().stream()
                .filter(member -> member.getId().equals(request.getUserId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not a member of the project"));

        project.setManager(newManager);

        projectRepository.save(project);

        return toResponse(project);
    }

    @Transactional
    public ProjectResponse updateMember(String id, User user, UpdateProjectMemberRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (!project.getManager().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update project members");
        }

        // TODO check if the user values are valid
        List<User> members = userRepository.findAllById(request.getUserIds());

        project.setMembers(members);
        projectRepository.save(project);

        return toResponse(project);
    }

    @Transactional
    public void deleteProject(String id, User user) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (!project.getManager().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this project");
        }

        projectRepository.delete(project);
    }
}
