package com.example.task_tracker_api.API.controller;


import com.example.task_tracker_api.API.dto.ProjectDTO;
import com.example.task_tracker_api.API.exception.BadRequestException;
import com.example.task_tracker_api.API.factories.ProjectDtoFactory;
import com.example.task_tracker_api.store.entities.ProjectEntity;
import com.example.task_tracker_api.store.repository.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping
public class ProjectController {

    ProjectDtoFactory projectDtoFactory;
    ProjectRepository projectRepository;

    public static final String CREATE_PROJECT = "/api/projects";

    @PostMapping(CREATE_PROJECT)
    public ProjectDTO createProject(@RequestParam String name) {

        projectRepository
                .findByName(name)
                .ifPresent(project -> {
                    throw new BadRequestException(
                            String.format("Project \"%s\" already exists.", name)
                    );
                });


        ProjectEntity project = projectRepository.saveAndFlush(
                ProjectEntity.builder()
                        .name(name)
                        .build()
        );

        return projectDtoFactory.makeProjectDto(project);

    }
}
