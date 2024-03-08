package com.example.task_tracker_api.store.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "task_state")
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class TaskStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    @Column(unique = true)
    String name;
    @Builder.Default
    Instant createdAt = Instant.now();
    Long ordinal;
    @Builder.Default
    @OneToMany
    @JoinColumn(name = "task_state_id", referencedColumnName = "id")
    List<TaskEntity> tasks = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskStateEntity that = (TaskStateEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(ordinal, that.ordinal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ordinal);
    }
}
