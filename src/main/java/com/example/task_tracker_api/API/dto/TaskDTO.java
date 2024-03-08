package com.example.task_tracker_api.API.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Builder
@Data
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class TaskDTO {

    @NonNull
    Long id;
    @NonNull
    String name;
    @NonNull
    @JsonProperty("created_at")
    Instant createdAt;
    @NonNull
    String description;

}
