package com.example.task_tracker_api.API.controller;

import com.example.task_tracker_api.API.controller.helper.ControllerHelper;
import com.example.task_tracker_api.API.dto.AskDTO;
import com.example.task_tracker_api.API.dto.TaskStateDTO;
import com.example.task_tracker_api.API.exception.BadRequestException;
import com.example.task_tracker_api.API.exception.NotFoundException;
import com.example.task_tracker_api.API.factories.TaskStateDtoFactory;
import com.example.task_tracker_api.store.entities.ProjectEntity;
import com.example.task_tracker_api.store.entities.TaskStateEntity;
import com.example.task_tracker_api.store.repository.TaskStateRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@Transactional
public class TaskStateController {

    TaskStateRepository taskStateRepository;

    TaskStateDtoFactory taskStateDtoFactory;

    ControllerHelper controllerHelper;


    public static final String GET_TASK_STATES = "/api/projects/{project_id}/task-states";
    public static final String CREATE_TASK_STATE = "/api/projects/{project_id}/task-states";
    public static final String UPDATE_TASK_STATE = "/api/task-states/{task_state_id}";
    public static final String CHANGE_TASK_STATE_POSITION = "/api/task-states/{task_state_id}/position/change";
    public static final String DELETE_TASK_STATE = "/api/task-states/{task_state_id}";




    @GetMapping(GET_TASK_STATES)
    public List<TaskStateDTO> getTaskStates(@PathVariable("project_id") Long projectId) {

        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);

        return project
                .getTaskState()
                .stream()
                .map(taskStateDtoFactory :: makeTaskStateDto)
                .toList();
    }

    @PostMapping(CREATE_TASK_STATE)
    public TaskStateDTO createTaskState(
            @PathVariable("project_id") Long projectId,
            @RequestParam("task_state_name") String taskStateName) {

        if (taskStateName.isBlank()) {
            throw new BadRequestException("Task state name can't be empty");
        }

        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);

        Optional<TaskStateEntity> optionalAnotherTaskState = Optional.empty();

        for (TaskStateEntity taskState : project.getTaskState()) {

            if (taskState.getName().equalsIgnoreCase(taskStateName)) {
                throw new BadRequestException(
                        String.format("Task State \"%s\" already exists.", taskStateName)
                );
            }

            if (!taskState.getRightTaskState().isPresent()) {
                optionalAnotherTaskState = Optional.of(taskState);
                break;
            }
        }

        TaskStateEntity taskState = taskStateRepository
                .saveAndFlush(TaskStateEntity.builder()
                        .name(taskStateName)
                        .project(project)
                        .build());


        optionalAnotherTaskState
                .ifPresent(anotherTaskState -> {

                    taskState.setLeftTaskState(anotherTaskState);

                    anotherTaskState.setRightTaskState(taskState);

                    taskStateRepository.saveAndFlush(anotherTaskState);
                });

        final TaskStateEntity savedTaskState = taskStateRepository.saveAndFlush(taskState);

        return taskStateDtoFactory.makeTaskStateDto(savedTaskState);
    }

    @PatchMapping(UPDATE_TASK_STATE)
    public TaskStateDTO updateTaskState(
            @PathVariable("task_state_id") Long taskStateId,
            @RequestParam("task_state_name") String taskStateName) {

        if (taskStateName.isBlank()) {
            throw new BadRequestException("Task state name can't be empty");
        }

        TaskStateEntity taskState = getTaskStateOrThrowException(taskStateId);

        taskStateRepository
                .findTaskStateEntityByProjectIdAndNameContainsIgnoreCase(
                        taskState.getProject().getId(),
                        taskStateName
                ).
                filter(anotherTaskState -> !anotherTaskState.getId().equals(taskStateId)).
                ifPresent(anotherTaskState -> {
                    throw new BadRequestException(
                            String.format("Task State \"%s\" already exists.", taskStateName));
                });


        taskState.setName(taskStateName);

       taskState = taskStateRepository.saveAndFlush(taskState);

        return taskStateDtoFactory.makeTaskStateDto(taskState);
    }

    @PatchMapping(CHANGE_TASK_STATE_POSITION)
    public TaskStateDTO changeTaskStatePosition(
            @PathVariable("task_state_id") Long taskStateId,
            @RequestParam("task_state_name") String taskStateName) {

        if (taskStateName.isBlank()) {
            throw new BadRequestException("Task state name can't be empty");
        }

        TaskStateEntity taskState = getTaskStateOrThrowException(taskStateId);

        taskStateRepository
                .findTaskStateEntityByProjectIdAndNameContainsIgnoreCase(
                        taskState.getProject().getId(),
                        taskStateName
                ).
                filter(anotherTaskState -> !anotherTaskState.getId().equals(taskStateId)).
                ifPresent(anotherTaskState -> {
                    throw new BadRequestException(
                            String.format("Task State \"%s\" already exists.", taskStateName));
                });


        taskState.setName(taskStateName);

        taskState = taskStateRepository.saveAndFlush(taskState);

        return taskStateDtoFactory.makeTaskStateDto(taskState);
    }

    @DeleteMapping(DELETE_TASK_STATE)
    public AskDTO deleteTaskState(@PathVariable("task_state_id") Long taskStateId) {

        TaskStateEntity changeTaskState = getTaskStateOrThrowException(taskStateId);

        replaceOldTaskStatePosition(changeTaskState);

        changeTaskState = taskStateRepository.saveAndFlush(changeTaskState);

        taskStateRepository.delete(changeTaskState);

        return AskDTO.makeDefault(true);
    }

    private void replaceOldTaskStatePosition(TaskStateEntity changeTaskState) {

        Optional<TaskStateEntity> optionalOldLeftTaskState = changeTaskState.getLeftTaskState();
        Optional<TaskStateEntity> optionalOldRightTaskState = changeTaskState.getRightTaskState();

        optionalOldLeftTaskState
                .ifPresent(it -> {
                    it.setRightTaskState(optionalOldRightTaskState.orElse(null));

                    taskStateRepository.saveAndFlush(it);
                });

        optionalOldRightTaskState
                .ifPresent(it -> {
                    it.setLeftTaskState(optionalOldLeftTaskState.orElse(null));

                    taskStateRepository.saveAndFlush(it);
                });
    }


    private TaskStateEntity getTaskStateOrThrowException(Long taskStateId) {
        return taskStateRepository
                .findById(taskStateId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Task status with %d not found", taskStateId))
                );
    }
}
