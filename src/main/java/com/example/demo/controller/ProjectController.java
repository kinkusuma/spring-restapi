package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.model.*;
import com.example.demo.service.ProjectService;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping(
            value = "/api/project",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProjectResponse> createProject(User user, @RequestBody CreateProjectRequest request){
        ProjectResponse projectResponse = projectService.createProject(user, request);
        return WebResponse.<ProjectResponse>builder()
                .data(projectResponse)
                .build();
    }

    @GetMapping(
            value = "/api/project/{projectId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProjectResponse> getProject(User user, @PathVariable("projectId") String projectId){
        ProjectResponse projectResponse = projectService.getProject(projectId, user);
        return WebResponse.<ProjectResponse>builder()
                .data(projectResponse)
                .build();
    }

    @PutMapping(
            value = "/api/project/{projectId}/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProjectResponse> updateProject(User user, @RequestBody UpdateProjectRequest request, @PathVariable("projectId") String projectId){
        ProjectResponse projectResponse = projectService.updateProject(projectId, user, request);
        return WebResponse.<ProjectResponse>builder()
                .data(projectResponse)
                .build();
    }

    @PutMapping(
            value = "/api/project/{projectId}/reasignManager",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProjectResponse> reassignManager(User user, @RequestBody ReassignProjectManagerRequest request, @PathVariable("projectId") String projectId){
        ProjectResponse projectResponse = projectService.reassignManager(projectId, user, request);
        return WebResponse.<ProjectResponse>builder()
                .data(projectResponse)
                .build();
    }

    @PutMapping(
            value = "/api/project/{projectId}/updateMembers",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProjectResponse> updateMembers(User user, @RequestBody UpdateProjectMemberRequest request, @PathVariable("projectId") String projectId){
        ProjectResponse projectResponse = projectService.updateMember(projectId, user, request);
        return WebResponse.<ProjectResponse>builder()
                .data(projectResponse)
                .build();
    }

    @DeleteMapping(
            value = "/api/project/{projectId}/delete",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteProject(User user, @PathVariable("projectId") String projectId){
        projectService.deleteProject(projectId, user);
        return WebResponse.<String>builder()
                .data("Project deleted successfully")
                .build();
    }
}
