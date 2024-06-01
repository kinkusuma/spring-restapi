package com.example.demo.model;

import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectRequest {
    private Optional<String> name;
    private Optional<String> description;
    private Optional<Integer> budget;


    @Future
    private Optional<LocalDateTime> deadline;

    private Optional<String> videoPresentationUrl;
    private Optional<String> imageUrl;
    private Optional<String> projectStatus;

}
