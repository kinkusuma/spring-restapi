package com.example.demo.service;

import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.model.*;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
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
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TaskResponse createTask(String projectId, User user, CreateTaskRequest request) {
        validationService.validate(request);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (!project.getMembers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this project");
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDeadline(request.getDeadline());
        task.setDocumentUrl(request.getDocumentUrl());
        task.setProject(project);

        taskRepository.save(task);

        return toResponse(task);
    }

    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .documentUrl(task.getDocumentUrl())
                .assignees(Optional.ofNullable(task.getAssignees()).orElse(Collections.emptyList()).stream()
                        .map(assignee -> UserResponse.builder()
                                .id(assignee.getId())
                                .name(assignee.getName())
                                .email(assignee.getEmail())
                                .build())
                        .toList())
                .build();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTask(String taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (!task.getProject().getMembers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this task");
        }

        return toResponse(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasks(String projectId, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (!project.getMembers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this project");
        }

        return project.getTasks().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskResponse updateTask(String taskId, User user, UpdateTaskRequest request) {
        validationService.validate(request);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (!task.getProject().getMembers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this task");
        }

        request.getTitle().ifPresent(task::setTitle);
        request.getDescription().ifPresent(task::setDescription);
        request.getDeadline().ifPresent(task::setDeadline);
        request.getDocumentUrl().ifPresent(task::setDocumentUrl);

        taskRepository.save(task);

        return toResponse(task);
    }

    @Transactional
    public TaskResponse updateTaskAssignees(String taskId, User user, UpdateTaskAssignees request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (!task.getProject().getMembers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this task");
        }

        // TODO check if the user values are valid
        List<User> assignees = userRepository.findAllById(request.getUserIds());

        task.setAssignees(assignees);

        taskRepository.save(task);

        return toResponse(task);
    }

    @Transactional
    public void deleteTask(String taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (!task.getProject().getMembers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this task");
        }

        taskRepository.delete(task);
    }
}
