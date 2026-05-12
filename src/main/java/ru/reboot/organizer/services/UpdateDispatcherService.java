package ru.reboot.organizer.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.reboot.organizer.handlers.telegram.TelegramHandler;
import ru.reboot.organizer.utils.dev.SessionManager;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateDispatcherService {
    private final SessionManager sessionManager;

    private final List<TelegramHandler> handlers;

    public BotApiMethod<?> dispatch(Update update) {
        Long chatId = extractChatId(update);
        if (chatId == null) return null;

        String currentState = sessionManager.getUserState(chatId);

        for (TelegramHandler handler : handlers) {
            if (handler.supports(update, currentState)) {
                return handler.handle(update, currentState, chatId);
            }
        }

        log.warn("Не найден обработчик для Update: {}", update);
        return null;
    }

    private Long extractChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        }
        return null;
    }
}
