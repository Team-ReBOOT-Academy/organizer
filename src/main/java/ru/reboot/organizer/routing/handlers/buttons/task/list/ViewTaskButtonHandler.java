package ru.reboot.organizer.routing.handlers.buttons.task.list;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.database.entity.Task;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.dto.UserScreens;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.services.SessionManagerService;
import ru.reboot.organizer.services.TaskService;
import ru.reboot.organizer.utils.MessageManager;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ViewTaskButtonHandler implements ButtonHandler {
    private final TaskService taskService;
    private final SessionManagerService sessionManagerService;
    private final MessageManager messageManager;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.TASK_LIST_VIEW_TASK;
    }

    @Override
    public UnifiedResponse handleButton(Long userId, String payload) {
        sessionManagerService.setUserScreen(userId, UserScreens.TASK_VIEW);

        String taskIdStr = payload.replace(ButtonType.TASK_LIST_VIEW_TASK.getPayload(), "");
        Long taskId = Long.parseLong(taskIdStr);

        Task task;
        try {
            task = taskService.getTask(taskId, userId);
        } catch (IllegalStateException e) {
            return UnifiedResponse.builder()
                    .text(messageManager.getMessage("error.task.get"))
                    .row().button(messageManager.getMessage("button.task.list.categories"), ButtonType.TASK_LIST_CATEGORIES.getPayload())
                    .build();
        }

        StringBuilder textBuilder = new StringBuilder();

        textBuilder.append(messageManager.getMessage("task.view.header", task.getTitle()));

        if (task.getDescription() != null && !task.getDescription().isBlank()) {
            textBuilder.append(messageManager.getMessage("task.view.description", task.getDescription()));
        }

        if (task.getDeadline() != null) {
            textBuilder.append(messageManager.getMessage("task.view.deadline", task.getDeadline().format(formatter)));
        }

        String importantButtonText = task.isImportant()
                ? messageManager.getMessage("button.task.unmark.important")
                : messageManager.getMessage("button.task.mark.important");

        String completeButtonText = task.isCompleted()
                ? messageManager.getMessage("button.task.unmark.completed")
                : messageManager.getMessage("button.task.mark.completed");

        return UnifiedResponse.builder()
                .text(textBuilder.toString().trim())
                .row().button(importantButtonText, ButtonType.TASK_TOGGLE_IMPORTANT.getPayload() + task.getId())
                .row().button(completeButtonText, ButtonType.TASK_TOGGLE_COMPLETED.getPayload() + task.getId())
                .row().button(messageManager.getMessage("button.task.list"), ButtonType.TASK_LIST_CATEGORIES.getPayload())
                .build();
    }
}
