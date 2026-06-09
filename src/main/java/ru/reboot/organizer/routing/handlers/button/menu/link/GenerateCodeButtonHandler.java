package ru.reboot.organizer.routing.handlers.button.menu.link;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.services.PlatformLinkService;

@Component
@RequiredArgsConstructor
public class GenerateCodeButtonHandler implements ButtonHandler {
    private final PlatformLinkService platformLinkService;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.GENERATE_LINK_CODE;
    }

    @Override
    public UnifiedResponse handleButton(Long userId, String payload) {
        String code = platformLinkService.getOrGenerateCodeForUser(userId);

        return UnifiedResponse.builder()
                .text("Код привязки:\n\n"
                        + code + "\n\n" +
                        "Код действует 10 минут. Введите его на другой платформе")
                .row().button("В главное меню", ButtonType.MAIN_MENU)
                .build();
    }
}
