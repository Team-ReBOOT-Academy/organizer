package ru.reboot.organizer.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.reboot.organizer.database.entity.AppUser;
import ru.reboot.organizer.database.entity.Task;
import ru.reboot.organizer.database.repository.AppUserRepository;
import ru.reboot.organizer.database.repository.TaskRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final AppUserRepository appUserRepository;
    private final TaskRepository taskRepository;
    private final SessionManagerService sessionManagerService;

    @Transactional
    public void createTaskFromDraft(Long userId, boolean isImportant) {
        AppUser appUser = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден при сохранении задачи"));

        SessionManagerService.TaskDraft draft = sessionManagerService.getOrCreateDraft(userId);

        if (draft.getTitle() == null || draft.getTitle().isBlank()) {
            throw new IllegalStateException("Тема задачи не может быть пустой");
        }

        Task task = new Task();
        task.setAppUser(appUser);
        task.setTitle(draft.getTitle());
        task.setDescription(draft.getDescription());
        task.setDeadline(draft.getDeadline());
        task.setImportant(isImportant);
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());

        taskRepository.save(task);

        sessionManagerService.clearDraft(userId);
    }

    @Transactional
    public Page<Task> getImportantTasks(Long userId, int pageNumber) {
        AppUser appUser = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));

        return taskRepository.findByAppUserAndIsImportantTrueAndIsCompletedFalseOrderByCreatedAtDesc(
                appUser, PageRequest.of(pageNumber, 5));
    }
}
