package ru.reboot.organizer.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.dto.UserRequest;
import ru.reboot.organizer.dto.UserScreens;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.routing.CommandHandler;
import ru.reboot.organizer.routing.ScreenHandler;
import ru.reboot.organizer.utils.MessageManager;

import java.util.HashMap;
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
    private final SessionManagerService sessionManagerService;
    private final MessageManager messageManager;

    private final Map<String, ScreenHandler> screenHandlers;
    private final Map<ButtonType, ButtonHandler> buttonHandlers;
    private final Map<String, CommandHandler> commandHandlers;

    public CoreRouterService(
            SessionManagerService sessionManagerService,
            MessageManager messageManager,
            List<ScreenHandler> screenHandlersList,
            List<ButtonHandler> buttonHandlersList,
            List<CommandHandler> commandHandlersList
    ) {
        this.sessionManagerService = sessionManagerService;
        this.messageManager = messageManager;

        this.screenHandlers = screenHandlersList.stream()
                .collect(Collectors.toMap(ScreenHandler::getHandledScreen, Function.identity()));

        this.buttonHandlers = buttonHandlersList.stream()
                .collect(Collectors.toMap(ButtonHandler::getHandledButton, Function.identity()));

        this.commandHandlers = new HashMap<>();
        for (CommandHandler handler : commandHandlersList) {
            for (String cmd : handler.getHandledCommands()) {
                this.commandHandlers.put(cmd.toLowerCase(), handler);
            }
        }

        log.info("CoreRouterService инициализирован. Обнаружено экранов ввода: {}, экранов с кнопками: {}, команд: {}",
                screenHandlers.size(), buttonHandlers.size(), commandHandlers.size());
    }

    public UnifiedResponse route(UserRequest request) {
        Long userId = request.globalUserId();
        String text = request.text();

        if (text == null || text.trim().isEmpty()) {
            return fallback(userId);
        }

        String cleanText = text.trim();
        if (cleanText.startsWith("/")) {
            String baseCommand = cleanText.split(" ")[0].toLowerCase();

            CommandHandler commandHandler = commandHandlers.get(baseCommand);
            if (commandHandler != null) {
                return commandHandler.handleCommand(userId, cleanText);
            } else {
                return fallback(userId);
            }
        }

        ButtonType button = ButtonType.fromPayload(cleanText);
        if (button != null) {
            ButtonHandler handler = buttonHandlers.get(button);
            if (handler != null) {
                return handler.handleButton(userId, cleanText);
            }
        }

        String currentScreen = sessionManagerService.getUserScreen(userId);
        ScreenHandler handler = screenHandlers.get(currentScreen);
        if (handler != null) {
            return handler.handleText(userId, cleanText);
        }

        return fallback(userId);
    }

    private UnifiedResponse fallback(Long userId) {
        sessionManagerService.setUserScreen(userId, UserScreens.FALLBACK);

        return UnifiedResponse.builder()
                .text(messageManager.getMessage("error.fallback"))
                .row().button(messageManager.getMessage("button.menu"), ButtonType.MAIN_MENU.getPayload())
                .build();
    }
}
