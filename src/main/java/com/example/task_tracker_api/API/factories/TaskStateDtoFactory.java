package com.example.task_tracker_api.API.factories;

import com.example.task_tracker_api.API.dto.TaskStateDTO;
import com.example.task_tracker_api.store.entities.TaskStateEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskStateDtoFactory {

    TaskDtoFactory taskDtoFactory;

    public TaskStateDTO makeTaskStateDto(TaskStateEntity entity) {

        return TaskStateDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .leftTaskStateId(entity.getLeftTaskState().map(TaskStateEntity::getId).orElse(null))
                .rightTaskStateId(entity.getRightTaskState().map(TaskStateEntity::getId).orElse(null))
                .tasks(
                        entity
                                .getTasks()
                                .stream()
                                .map(taskDtoFactory :: makeTaskDto)
                                .toList()
                )
                .build();
    }

}
