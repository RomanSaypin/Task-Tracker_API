package com.example.task_tracker_api.store.repository;

import com.example.task_tracker_api.store.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByName(String name);
}
