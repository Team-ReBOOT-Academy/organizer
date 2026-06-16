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
public class GenerateLinkCodeButtonHandler implements ButtonHandler {
    private final MessageManager messageManager;
    private final SessionManagerService sessionManagerService;
    private final PlatformLinkService platformLinkService;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.GENERATE_LINK_CODE;
    }

    @Override
    public UnifiedResponse handleButton(Long userId, String payload) {
        sessionManagerService.setUserScreen(userId, UserScreens.LINK_PLATFORM_GENERATE_CODE);

        String code = platformLinkService.generateAndSaveCode(userId);
        return UnifiedResponse.builder()
                .text(messageManager.getMessage("link.code.generate.success", code))
                .row().button(messageManager.getMessage("button.menu"), ButtonType.MAIN_MENU.getPayload())
                .build();
    }
}
