package ru.reboot.organizer.routing.handlers.button.menu.link;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.utils.dev.SessionManager;
import ru.reboot.organizer.utils.dev.UserScreens;

@Component
@RequiredArgsConstructor
public class LinkPlatformButtonHandler implements ButtonHandler {
    private final SessionManager sessionManager;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.LINK_PLATFORM;
    }

    @Override
    public UnifiedResponse handleButton(Long userId) {
        sessionManager.setUserScreen(userId, UserScreens.LINK_PLATFORM_MENU);

        return UnifiedResponse.builder()
                .text("Подключение платформы")
                .row()
                .button("Сгенерировать код", ButtonType.GENERATE_LINK_CODE)
                .row()
                .button("Ввести код", ButtonType.ENTER_LINK_CODE)
                .row()
                .button("В главное меню", ButtonType.MAIN_MENU)
                .build();
    }
}
