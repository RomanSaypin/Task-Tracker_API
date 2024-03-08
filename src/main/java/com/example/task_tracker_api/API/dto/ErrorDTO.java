package com.example.task_tracker_api.API.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {

    String error;
    @JsonProperty("error_Description")
    String errorDescription;
}
