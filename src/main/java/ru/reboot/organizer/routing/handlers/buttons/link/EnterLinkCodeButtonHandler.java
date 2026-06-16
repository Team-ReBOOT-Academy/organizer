package ru.reboot.organizer.routing.handlers.buttons.link;

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
public class EnterLinkCodeButtonHandler implements ButtonHandler {
    private final SessionManagerService sessionManagerService;
    private final MessageManager messageManager;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.ENTER_LINK_CODE;
    }

    @Override
    public UnifiedResponse handleButton(Long userId, String payload) {
        sessionManagerService.setUserScreen(userId, UserScreens.LINK_PLATFORM_ENTER_CODE);

        return UnifiedResponse.builder()
                .text(messageManager.getMessage("link.enter.prompt"))
                .row().button(messageManager.getMessage("button.menu"), ButtonType.MAIN_MENU.getPayload())
                .build();
    }
}
