package com.example.task_tracker_api.API.controller;


import com.example.task_tracker_api.API.dto.AskDTO;
import com.example.task_tracker_api.API.dto.ProjectDTO;
import com.example.task_tracker_api.API.exception.BadRequestException;
import com.example.task_tracker_api.API.exception.NotFoundException;
import com.example.task_tracker_api.API.factories.ProjectDtoFactory;
import com.example.task_tracker_api.store.entities.ProjectEntity;
import com.example.task_tracker_api.store.repository.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping
public class ProjectController {

    ProjectDtoFactory projectDtoFactory;
    ProjectRepository projectRepository;



    public static final String CREATE_OR_UPDATE_PROJECT = "/api/projects";
    public static final String DELETE_PROJECT = "/api/projects/{project_id}";
    public static final String FETCH_PROJECT = "/api/projects";

    @GetMapping(FETCH_PROJECT)
    public List<ProjectDTO> fetchProject(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

        optionalPrefixName = optionalPrefixName
                .filter(prefixName -> !prefixName.trim().isEmpty());

        Stream<ProjectEntity> projectStream = optionalPrefixName
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAllBy);

        return projectStream
                .map(projectDtoFactory::makeProjectDto)
                .toList();
    }

    @PutMapping(CREATE_OR_UPDATE_PROJECT)
    public ProjectDTO createOrUpdateProject(
            @RequestParam(value = "project_id", required = false) Optional<Long> optionalProjectId,
            @RequestParam(value = "project_name", required = false) Optional<String> optionalProjectName
    ) {
        boolean isCreate = optionalProjectId.isEmpty();

        if (isCreate && optionalProjectName.isEmpty()) {
            throw new BadRequestException("Project name can't be empty");
        }

        ProjectEntity project = optionalProjectId
                .map(this::getProjectOrThrowException)
                .orElseGet(() -> ProjectEntity.builder().build());

        optionalProjectName = optionalProjectName.filter(projectName -> !projectName.trim().isEmpty());

       optionalProjectName
               .ifPresent(projectName -> {

                   projectRepository
                           .findByName(projectName)
                           .filter(anotherProject -> !Objects.equals(anotherProject.getId(), project.getId()))
                           .ifPresent(anotherProject -> {
                               throw new BadRequestException(
                                       String.format("Project \"%s\" already exists.", projectName)
                               );
                           });
                   project.setName(projectName);
               });

       final ProjectEntity savedProject = projectRepository.saveAndFlush(project);

        return projectDtoFactory.makeProjectDto(savedProject);
    }

    @DeleteMapping(DELETE_PROJECT)
    public AskDTO deleteProject(@PathVariable("project_id") Long projectId) {

        ProjectEntity project = getProjectOrThrowException(projectId);

        projectRepository.deleteById(projectId);

        return AskDTO.makeDefault(true);
    }

    private ProjectEntity getProjectOrThrowException(Long id) {
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
