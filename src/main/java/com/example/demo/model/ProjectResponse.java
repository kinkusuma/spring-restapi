package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    private String id;
    private String name;
    private String description;
    private Integer budget;
    private LocalDateTime deadline;
    private String videoPresentationUrl;
    private String imageUrl;
    private String projectStatus;
    private UserResponse projectManager;
    private List<UserResponse> members;
    private List<TaskResponse> tasks;

}
