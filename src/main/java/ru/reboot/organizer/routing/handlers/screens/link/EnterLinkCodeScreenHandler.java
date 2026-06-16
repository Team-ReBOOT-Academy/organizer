package ru.reboot.organizer.routing.handlers.screens.link;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.dto.UserScreens;
import ru.reboot.organizer.routing.ScreenHandler;
import ru.reboot.organizer.services.PlatformLinkService;
import ru.reboot.organizer.services.SessionManagerService;
import ru.reboot.organizer.utils.MessageManager;

@Component
@RequiredArgsConstructor
public class EnterLinkCodeScreenHandler implements ScreenHandler {
    private final PlatformLinkService platformLinkService;
    private final SessionManagerService sessionManagerService;
    private final MessageManager messageManager;

    @Override
    public String getHandledScreen() {
        return UserScreens.LINK_PLATFORM_ENTER_CODE;
    }

    @Override
    public UnifiedResponse handleText(Long userId, String text) {
        String code = text.trim();
        try {
            platformLinkService.validateCodeAndPrepareMerge(code, userId);

            sessionManagerService.setUserScreen(userId, UserScreens.MAIN_MENU);
            return UnifiedResponse.builder()
                    .text(messageManager.getMessage("link.approval.wait"))
                    .row().button(messageManager.getMessage("button.menu"), ButtonType.MAIN_MENU.getPayload())
                    .build();
        } catch (Exception e) {
            return UnifiedResponse.builder()
                    .text(messageManager.getMessage("error.link.code"))
                    .row().button(messageManager.getMessage("button.menu"), ButtonType.MAIN_MENU.getPayload())
                    .build();
        }
    }
}
