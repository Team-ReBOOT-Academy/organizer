package ru.reboot.organizer.routing.handlers.buttons.task.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.dto.UserScreens;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.services.SessionManagerService;
import ru.reboot.organizer.services.TaskService;
import ru.reboot.organizer.utils.MessageManager;

@Component
@RequiredArgsConstructor
public class NewTaskImportantYesButtonHandler implements ButtonHandler {
    private final SessionManagerService sessionManagerService;
    private final MessageManager messageManager;
    private final TaskService taskService;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.NEW_TASK_IMPORTANT_YES;
    }

    @Override
    public UnifiedResponse handleButton(Long userId, String payload) {
        taskService.createTaskFromDraft(userId, true);

        sessionManagerService.setUserScreen(userId, UserScreens.MAIN_MENU);

        return UnifiedResponse.builder()
                .text(messageManager.getMessage("task.new.success"))
                .row().button(messageManager.getMessage("button.menu.task.list"), ButtonType.TASK_LIST.getPayload())
                .row().button(messageManager.getMessage("button.menu"), ButtonType.MAIN_MENU.getPayload())
                .build();
    }
}
