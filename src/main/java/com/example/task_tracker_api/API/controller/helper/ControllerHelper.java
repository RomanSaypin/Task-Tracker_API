package com.example.task_tracker_api.API.controller.helper;

import com.example.task_tracker_api.API.exception.NotFoundException;
import com.example.task_tracker_api.store.entities.ProjectEntity;
import com.example.task_tracker_api.store.repository.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Transactional
public class ControllerHelper {

    ProjectRepository projectRepository;

    public ProjectEntity getProjectOrThrowException(Long id) {
        return projectRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "The project with id \"%s\" was not found", id
                                )
                        )
                );
    }
}
