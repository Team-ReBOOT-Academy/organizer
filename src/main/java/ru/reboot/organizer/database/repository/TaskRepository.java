package ru.reboot.organizer.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.reboot.organizer.database.entity.AppUser;
import org.springframework.stereotype.Repository;
import ru.reboot.organizer.database.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByAppUserAndIsImportantTrueAndIsCompletedFalseOrderByCreatedAtDesc(AppUser appUser, Pageable pageable);
    Page<Task> findByAppUserAndIsImportantFalseAndIsCompletedFalseOrderByCreatedAtDesc(AppUser appUser, Pageable pageable);
    Page<Task> findByAppUserAndIsCompletedTrueOrderByCreatedAtDesc(AppUser appUser, Pageable pageable);
}