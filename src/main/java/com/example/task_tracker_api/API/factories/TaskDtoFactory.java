package com.example.task_tracker_api.API.factories;

import com.example.task_tracker_api.API.dto.TaskDTO;
import com.example.task_tracker_api.store.entities.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoFactory {

    public TaskDTO makeTaskDto(TaskEntity entity) {

        return TaskDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .description(entity.getDescription())
                .build();
    }
}
