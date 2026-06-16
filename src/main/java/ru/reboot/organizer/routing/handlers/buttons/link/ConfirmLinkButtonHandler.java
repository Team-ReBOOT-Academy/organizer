package ru.reboot.organizer.routing.handlers.buttons.link;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.dto.UserScreens;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.services.PlatformLinkService;
import ru.reboot.organizer.services.SessionManagerService;
import ru.reboot.organizer.utils.MessageManager;

@Component
@RequiredArgsConstructor
public class ConfirmLinkButtonHandler implements ButtonHandler {
    private final PlatformLinkService platformLinkService;
    private final SessionManagerService sessionManagerService;
    private final MessageManager messageManager;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.CONFIRM_LINK;
    }

    @Override
    public UnifiedResponse handleButton(Long userId, String payload) {
        if (!UserScreens.LINK_PLATFORM_APPROVAL.equals(sessionManagerService.getUserScreen(userId))) {
            return UnifiedResponse.builder()
                    .text(messageManager.getMessage("link.code.expired"))
                    .row().button(messageManager.getMessage("button.menu"), ButtonType.MAIN_MENU.getPayload())
                    .build();
        }

        Long secondaryUserId = Long.parseLong(payload.replace(ButtonType.CONFIRM_LINK.getPayload(), ""));

        platformLinkService.mergeAccounts(userId, secondaryUserId);
        sessionManagerService.setUserScreen(userId, UserScreens.MAIN_MENU);

        return UnifiedResponse.builder()
                .text(messageManager.getMessage("link.notification.success"))
                .row().button(messageManager.getMessage("button.menu"), ButtonType.MAIN_MENU.getPayload())
                .build();
    }
}
