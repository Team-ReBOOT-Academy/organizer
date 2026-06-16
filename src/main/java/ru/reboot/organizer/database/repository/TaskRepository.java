package ru.reboot.organizer.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.reboot.organizer.database.entity.AppUser;
import org.springframework.stereotype.Repository;
import ru.reboot.organizer.database.entity.Task;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByAppUserAndIsImportantTrueAndIsCompletedFalseOrderByCreatedAtDesc(AppUser appUser, Pageable pageable);
    Page<Task> findByAppUserAndIsImportantFalseAndIsCompletedFalseOrderByCreatedAtDesc(AppUser appUser, Pageable pageable);
    Page<Task> findByAppUserAndIsCompletedTrueOrderByCreatedAtDesc(AppUser appUser, Pageable pageable);
    Optional<Task> findTaskByIdAndAppUser(Long taskId, AppUser appUser);

    @Modifying
    @Query("UPDATE Task t SET t.appUser = :primaryUser WHERE t.appUser = :secondaryUser")
    void reassignAllTasks(@Param("primaryUser") AppUser primaryUser, @Param("secondaryUser") AppUser secondaryUser);
}