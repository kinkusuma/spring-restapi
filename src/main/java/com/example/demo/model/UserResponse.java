package com.example.demo.model;

import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String email;
    private String name;
    //private List<Project> managedProject;
    //private List<Project> projects;
    //private List<Task> tasks;
}
