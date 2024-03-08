package com.example.task_tracker_api.API.factories;

import com.example.task_tracker_api.API.dto.TaskStateDTO;
import com.example.task_tracker_api.store.entities.TaskStateEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskStateDtoFactory {

    public TaskStateDTO makeTaskStateDto(TaskStateEntity entity) {

        return TaskStateDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .ordinal(entity.getOrdinal())
                .build();
    }

}
