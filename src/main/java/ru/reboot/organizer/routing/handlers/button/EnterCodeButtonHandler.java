package ru.reboot.organizer.routing.handlers.button;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.utils.dev.SessionManager;
import ru.reboot.organizer.utils.dev.UserScreens;

@Component
@RequiredArgsConstructor
public class EnterCodeButtonHandler implements ButtonHandler {
    private final SessionManager sessionManager;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.ENTER_LINK_CODE;
    }

    @Override
    public UnifiedResponse handleButton(Long userId) {
        sessionManager.setUserScreen(userId, UserScreens.PLATFORM_LINK_INPUT);

        return UnifiedResponse.builder()
                .text("Введите 6-значный код привязки:")
                .row()
                .button("Отмена", ButtonType.MAIN_MENU)
                .build();
    }
}
