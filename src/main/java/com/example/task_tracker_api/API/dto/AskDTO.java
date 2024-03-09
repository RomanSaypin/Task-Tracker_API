package com.example.task_tracker_api.API.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class AskDTO {

    Boolean answer;

    public static AskDTO makeDefault(Boolean answer) {
        return builder()
                .answer(answer)
                .build();
    }
}
