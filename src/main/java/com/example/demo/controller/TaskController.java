package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.model.CreateTaskRequest;
import com.example.demo.model.TaskResponse;
import com.example.demo.model.UpdateTaskAssignees;
import com.example.demo.model.WebResponse;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;

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

    @PutMapping(
            value = "/api/task/{taskId}/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaskResponse> updateTask(User user, @PathVariable("taskId") String taskId, @RequestBody CreateTaskRequest request){
        TaskResponse taskResponse = taskService.updateTask(taskId, user, request);
        return WebResponse.<TaskResponse>builder()
                .data(taskResponse)
                .build();
    }

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
