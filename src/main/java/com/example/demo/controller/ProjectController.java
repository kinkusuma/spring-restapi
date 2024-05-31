package com.example.demo.controller;

import com.example.demo.model.CreateProjectRequest;
import com.example.demo.model.ProjectResponse;
import com.example.demo.model.UpdateProjectRequest;
import com.example.demo.model.WebResponse;
import com.example.demo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping(
            value = "/api/projects",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProjectResponse> create(@RequestBody CreateProjectRequest request) {
        ProjectResponse response = projectService.createProject(request);
        return WebResponse.<ProjectResponse>builder()
                .data(response)
                .build();
    }

    @GetMapping(
            value = "/api/projects/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProjectResponse> get(@PathVariable String id) {
        ProjectResponse response = projectService.getProject(id);
        return WebResponse.<ProjectResponse>builder()
                .data(response)
                .build();
    }

    @PutMapping(
            value = "/api/projects/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProjectResponse> update(@PathVariable String id, @RequestBody UpdateProjectRequest request) {
        ProjectResponse response = projectService.updateProject(id, request);
        return WebResponse.<ProjectResponse>builder()
                .data(response)
                .build();
    }

    @DeleteMapping(
            value = "/api/projects/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(@PathVariable String id) {
        projectService.deleteProject(id);
        return WebResponse.<String>builder()
                .data("OK")
                .build();
    }
}
