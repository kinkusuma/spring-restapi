package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectRequest {
    private Optional<String> name;
    private Optional<String> description;
    private Optional<Integer> budget;
    private Optional<String> deadline;
    private Optional<String> videoPresentationUrl;
    private Optional<String> imageUrl;
    private Optional<String> projectStatus;

}
