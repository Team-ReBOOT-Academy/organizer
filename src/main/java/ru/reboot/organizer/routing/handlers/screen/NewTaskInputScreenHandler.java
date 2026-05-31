package ru.reboot.organizer.routing.handlers.screen;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.routing.ScreenHandler;
import ru.reboot.organizer.utils.dev.SessionManager;
import ru.reboot.organizer.utils.dev.UserScreens;

@Component
@RequiredArgsConstructor
public class NewTaskInputScreenHandler implements ScreenHandler {
    private final SessionManager sessionManager;

    @Override
    public String getHandledScreen() {
        return UserScreens.NEW_TASK_INPUT;
    }

    @Override
    public UnifiedResponse handleText(Long userId, String text) {
        sessionManager.setUserScreen(userId, UserScreens.MAIN_MENU);

        return UnifiedResponse.builder()
                .text("Задача " + text + " сохранена.\nГлавное меню:")
                .row()
                .button("Новая задача", ButtonType.NEW_TASK)
                .button("Мои задачи", ButtonType.TASK_LIST)
                .row()
                .button("Подключить платформу", ButtonType.LINK_PLATFORM)
                .build();
    }
}
