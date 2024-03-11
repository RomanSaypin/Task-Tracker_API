package com.example.task_tracker_api.store.repository;

import com.example.task_tracker_api.store.entities.ProjectEntity;
import com.example.task_tracker_api.store.entities.TaskStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {


    Optional<TaskStateEntity> findTaskStateEntityByProjectIdAndNameContainsIgnoreCase(Long projectId,
                                                                                          String taskStateName);
}
