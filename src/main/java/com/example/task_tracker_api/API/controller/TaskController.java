package com.example.task_tracker_api.API.controller;

import com.example.task_tracker_api.API.factories.TaskDtoFactory;
import com.example.task_tracker_api.store.repository.TaskRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@Transactional
public class TaskController {

    TaskRepository taskRepository;
    TaskDtoFactory taskDtoFactory;

    public static final String CREATE_OR_UPDATE_PROJECT = "/api/projects";
    public static final String DELETE_PROJECT = "/api/projects/{project_id}";
    public static final String FETCH_PROJECT = "/api/projects";


}
