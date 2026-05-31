package ru.reboot.organizer.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.reboot.organizer.database.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}