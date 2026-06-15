package ru.reboot.organizer.routing.handlers.screens.task.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.dto.UserScreens;
import ru.reboot.organizer.routing.ScreenHandler;
import ru.reboot.organizer.services.SessionManagerService;
import ru.reboot.organizer.utils.MessageManager;

@Component
@RequiredArgsConstructor
public class NewTaskInputThemeScreenHandler implements ScreenHandler {
    private final SessionManagerService sessionManagerService;
    private final MessageManager messageManager;

    @Override
    public String getHandledScreen() {
        return UserScreens.NEW_TASK_INPUT_THEME;
    }

    @Override
    public UnifiedResponse handleText(Long userId, String text) {
        SessionManagerService.TaskDraft draft = sessionManagerService.getOrCreateDraft(userId);
        draft.setTitle(text);

        sessionManagerService.setUserScreen(userId, UserScreens.NEW_TASK_INPUT_DESCRIPTION);

        return UnifiedResponse.builder()
                .text(messageManager.getMessage("task.new.description"))
                .row()
                    .button(messageManager.getMessage("button.task.new.description.skip"), ButtonType.NEW_TASK_SKIP_DESCRIPTION.getPayload())
                .row()
                    .button(messageManager.getMessage("button.task.new.cancel"), ButtonType.MAIN_MENU.getPayload())
                .build();
    }
}
