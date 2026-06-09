package ru.reboot.organizer.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.dto.UserRequest;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.routing.ScreenHandler;
import ru.reboot.organizer.utils.dev.SessionManager;
import ru.reboot.organizer.utils.dev.UserScreens;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Центральный сервис выбора обработчика экрана
 */

@Slf4j
@Service
public class CoreRouterService {
    private final SessionManager sessionManager;

    private final Map<String, ScreenHandler> screenHandlers;
    private final Map<ButtonType, ButtonHandler> buttonHandlers;

    public CoreRouterService(
            SessionManager sessionManager,
            List<ScreenHandler> screenHandlersList,
            List<ButtonHandler> buttonHandlersList
    ) {
        this.sessionManager = sessionManager;

        this.screenHandlers = screenHandlersList.stream()
                .collect(Collectors.toMap(ScreenHandler::getHandledScreen, Function.identity()));

        this.buttonHandlers = buttonHandlersList.stream()
                .collect(Collectors.toMap(ButtonHandler::getHandledButton, Function.identity()));

        log.info("CoreRouterService инициализирован. Обнаружено экранов: {}, кнопок: {}",
                screenHandlers.size(), buttonHandlers.size());
    }

    public UnifiedResponse route(UserRequest request) {
        Long userId = request.globalUserId();
        String text = request.text();

        if ("/start".equals(text) || "СТАРТ".equalsIgnoreCase(text)) {
            return buildHelloMessage(userId);
        } else if ("/menu".equals(text)) {
            return buildMainMenu(userId);
        }

        ButtonType button = ButtonType.fromPayload(text);
        if (button != null) {
            ButtonHandler handler = buttonHandlers.get(button);
            if (handler != null) {
                return handler.handleButton(userId, text);
            }
        }

        String currentScreen = sessionManager.getUserScreen(userId);
        ScreenHandler handler = screenHandlers.get(currentScreen);
        if (handler != null) {
            return handler.handleText(userId, text);
        }

        return buildMainMenu(userId);
    }

    private UnifiedResponse buildHelloMessage(Long userId) {
        sessionManager.setUserScreen(userId, UserScreens.DEFAULT_SCREEN);

        return UnifiedResponse.builder()
                .text("Вы начали общение с ботом, перейдите в главное меню")
                .row()
                .button("В главное меню", ButtonType.MAIN_MENU)
                .build();
    }

    private UnifiedResponse buildMainMenu(Long userId) {
        sessionManager.setUserScreen(userId, UserScreens.MAIN_MENU);

        return UnifiedResponse.builder()
                .text("Главное меню")
                .row()
                .button("Новая задача", ButtonType.NEW_TASK)
                .button("Мои задачи", ButtonType.TASK_LIST)
                .row()
                .button("Подключить платформу", ButtonType.LINK_PLATFORM)
                .build();
    }
}
