package com.example.demo.model;

import com.example.demo.entity.EProjectStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectRequest {
    @NotBlank
    @Size(max = 255)
    private String name;

    private String description;
    private Integer budget;

    @Future
    private LocalDateTime deadline;

    private String videoPresentationUrl;
    private String imageUrl;
}
