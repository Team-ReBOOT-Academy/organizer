package ru.reboot.organizer.routing.handlers.buttons.task.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.dto.UserScreens;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.services.SessionManagerService;
import ru.reboot.organizer.utils.MessageManager;

@Component
@RequiredArgsConstructor
public class NewTaskSkipDeadlineButtonHandler implements ButtonHandler {
    private final SessionManagerService sessionManagerService;
    private final MessageManager messageManager;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.NEW_TASK_SKIP_DEADLINE;
    }

    @Override
    public UnifiedResponse handleButton(Long userId, String payload) {
        sessionManagerService.setUserScreen(userId, UserScreens.NEW_TASK_MARK_IMPORTANT);

        return UnifiedResponse.builder()
                .text(messageManager.getMessage("task.new.important"))
                .row()
                .button(messageManager.getMessage("button.task.new.important.yes"), ButtonType.NEW_TASK_IMPORTANT_YES.getPayload())
                .button(messageManager.getMessage("button.task.new.important.no"), ButtonType.NEW_TASK_IMPORTANT_NO.getPayload())
                .row().button(messageManager.getMessage("button.task.new.cancel"), ButtonType.MAIN_MENU.getPayload())
                .build();
    }
}
