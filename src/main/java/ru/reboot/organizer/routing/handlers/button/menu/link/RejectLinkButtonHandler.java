package ru.reboot.organizer.routing.handlers.button.menu.link;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.services.PlatformLinkService;

@Component
@RequiredArgsConstructor
public class RejectLinkButtonHandler implements ButtonHandler {
    private final PlatformLinkService platformLinkService;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.REJECT_LINK;
    }

    @Override
    public UnifiedResponse handleButton(Long userId, String payload) {
        platformLinkService.rejectLink(userId);

        return UnifiedResponse.builder()
                .text("🛡 Подключение отклонено. Безопасность превыше всего.")
                .row().button("В главное меню", ButtonType.MAIN_MENU)
                .build();
    }
}
