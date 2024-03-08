package com.example.task_tracker_api.API.factories;

import com.example.task_tracker_api.API.dto.ProjectDTO;
import com.example.task_tracker_api.store.entities.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoFactory {

    public ProjectDTO makeProjectDto(ProjectEntity entity) {

        return ProjectDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
