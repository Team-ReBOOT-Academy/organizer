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
public class NewTaskButtonHandler implements ButtonHandler {
    private final SessionManager sessionManager;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.NEW_TASK;
    }

    @Override
    public UnifiedResponse handleButton(Long userId) {
        sessionManager.setUserScreen(userId, UserScreens.NEW_TASK_INPUT);

        return UnifiedResponse.builder()
                .text("Введите название задачи")
                .row().button("В главное меню", ButtonType.MAIN_MENU)
                .build();
    }
}
