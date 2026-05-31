package ru.reboot.organizer.routing.handlers.button.menu.link;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.services.PlatformLinkService;

@Component
@RequiredArgsConstructor
public class ConfirmLinkButtonHandler implements ButtonHandler {
    private final PlatformLinkService platformLinkService;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.CONFIRM_LINK;
    }

    @Override
    public UnifiedResponse handleButton(Long userId) {
        boolean isMerged = platformLinkService.confirmLink(userId);

        if (isMerged) {
            return UnifiedResponse.builder()
                    .text("✅ Платформы успешно связаны! Теперь у вас общая база задач.")
                    .row().button("В главное меню", ButtonType.MAIN_MENU)
                    .build();
        } else {
            return UnifiedResponse.builder()
                    .text("❌ Заявка на привязку не найдена или устарела.")
                    .row().button("В главное меню", ButtonType.MAIN_MENU)
                    .build();
        }
    }
}
