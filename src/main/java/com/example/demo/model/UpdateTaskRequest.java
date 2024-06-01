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
public class UpdateTaskRequest {
    private Optional<String> title;
    private Optional<String> description;

    @Future
    private Optional<LocalDateTime> deadline;

    private Optional<String> documentUrl;
}
