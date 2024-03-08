package com.example.task_tracker_api.API.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ProjectDTO {

    @NonNull
    Long id;
    @NonNull
    String name;
    @NonNull
    @JsonProperty("created_at")
    Instant createdAt;

}
