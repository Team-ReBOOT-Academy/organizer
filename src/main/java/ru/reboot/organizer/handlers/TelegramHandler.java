package ru.reboot.organizer.handlers;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Интерфейс для handler'ов
 */
public interface TelegramHandler {
    Boolean supports(Update update, String currentState);

    BotApiMethod<?> handle(Update update, String currentState, Long chatId);
}
