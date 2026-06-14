package ru.reboot.organizer.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.reboot.organizer.database.entity.AppUser;
import ru.reboot.organizer.database.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // 1. ВАЖНЫЕ: Важные (true) + Невыполненные (false) + Сортировка по убыванию даты
    Page<Task> findByAppUserAndIsImportantTrueAndIsCompletedFalseOrderByCreatedAtDesc(AppUser appUser, Pageable pageable);

    // 2. ОСТАЛЬНЫЕ: Неважные (false) + Невыполненные (false) + Сортировка по убыванию даты
    Page<Task> findByAppUserAndIsImportantFalseAndIsCompletedFalseOrderByCreatedAtDesc(AppUser appUser, Pageable pageable);

    // 3. ВЫПОЛНЕННЫЕ: Выполненные (true) + Сортировка по убыванию даты
    Page<Task> findByAppUserAndIsCompletedTrueOrderByCreatedAtDesc(AppUser appUser, Pageable pageable);
}