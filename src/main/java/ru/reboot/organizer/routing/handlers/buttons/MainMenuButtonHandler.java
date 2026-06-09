package ru.reboot.organizer.routing.handlers.buttons;

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
public class MainMenuButtonHandler implements ButtonHandler {
    private final MessageManager messageManager;
    private final SessionManagerService sessionManagerService;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.MAIN_MENU;
    }

    @Override
    public UnifiedResponse handleButton(Long userId, String payload) {
        sessionManagerService.setUserScreen(userId, UserScreens.MAIN_MENU);

        return UnifiedResponse.builder()
                .text(messageManager.getMessage("menu.header"))
                .row()
                    .button(messageManager.getMessage("button.menu.task.new"), ButtonType.NEW_TASK.getPayload())
                    .button(messageManager.getMessage("button.menu.task.list"), ButtonType.TASK_LIST.getPayload())
                .row()
                    .button(messageManager.getMessage("button.menu.connection"), ButtonType.LINK_PLATFORM.getPayload())
                .build();
    }
}
