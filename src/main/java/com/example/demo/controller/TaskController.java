package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.model.*;
import com.example.demo.service.TaskService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Parameter(name = "user", hidden = true)
    @PostMapping(
            value = "/api/project/{projectId}/task",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaskResponse> createTask(User user, @PathVariable("projectId") String projectId, @RequestBody CreateTaskRequest request){
        TaskResponse taskResponse = taskService.createTask(projectId, user, request);
        return WebResponse.<TaskResponse>builder()
                .data(taskResponse)
                .build();
    }

    @Parameter(name = "user", hidden = true)
    @GetMapping(
            value = "/api/project/{projectId}/task",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<TaskResponse>> getTasks(User user, @PathVariable("projectId") String projectId){
        List<TaskResponse> taskResponses = taskService.getTasks(projectId, user);
        return WebResponse.<List<TaskResponse>>builder()
                .data(taskResponses)
                .build();
    }

    @Parameter(name = "user", hidden = true)
    @GetMapping(
            value = "/api/task/{taskId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaskResponse> getTask(User user, @PathVariable("taskId") String taskId){
        TaskResponse taskResponse = taskService.getTask(taskId, user);
        return WebResponse.<TaskResponse>builder()
                .data(taskResponse)
                .build();
    }

    @Parameter(name = "user", hidden = true)
    @PutMapping(
            value = "/api/task/{taskId}/assign",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaskResponse> assignTask(User user, @PathVariable("taskId") String taskId, @RequestBody UpdateTaskAssignees request){
        TaskResponse taskResponse = taskService.updateTaskAssignees(taskId, user, request);
        return WebResponse.<TaskResponse>builder()
                .data(taskResponse)
                .build();
    }

    @Parameter(name = "user", hidden = true)
    @PutMapping(
            value = "/api/task/{taskId}/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaskResponse> updateTask(User user, @PathVariable("taskId") String taskId, @RequestBody UpdateTaskRequest request){
        TaskResponse taskResponse = taskService.updateTask(taskId, user, request);
        return WebResponse.<TaskResponse>builder()
                .data(taskResponse)
                .build();
    }

    @Parameter(name = "user", hidden = true)
    @DeleteMapping(
            value = "/api/task/{taskId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(User user, @PathVariable("taskId") String taskId){
        taskService.deleteTask(taskId, user);
        return WebResponse.<String>builder()
                .data("Task deleted successfully")
                .build();
    }
}
