package ru.reboot.organizer.routing.handlers.buttons.task.list;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.services.TaskService;

@Component
@RequiredArgsConstructor
public class TaskToggleImportantButtonHandler implements ButtonHandler {
    private final TaskService taskService;
    private final ViewTaskButtonHandler viewTaskButtonHandler;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.TASK_TOGGLE_IMPORTANT;
    }

    @Override
    public UnifiedResponse handleButton(Long userId, String payload) {
        String taskIdStr = payload.replace(ButtonType.TASK_TOGGLE_IMPORTANT.getPayload(), "");
        Long taskId = Long.parseLong(taskIdStr);

        taskService.toggleTaskImportance(taskId, userId);

        String viewPayload = ButtonType.TASK_LIST_VIEW_TASK.getPayload() + taskId;
        return viewTaskButtonHandler.handleButton(userId, viewPayload);
    }
}
