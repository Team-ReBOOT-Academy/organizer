package ru.reboot.organizer.mappers.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.reboot.organizer.dto.UserRequest;

/**
 * Маппер входящих сообщений для Telegram
 */

@Component
public class TelegramRequestMapper {
    public UserRequest map (Update update) {
        Long chatId = -1L;
        String text = "";

        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            text = update.getMessage().getText();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            text = update.getCallbackQuery().getData();
        }

        return new UserRequest(chatId, text, "TELEGRAM");
    }

    public Integer extractMessageIdForEdit(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getMessageId();
        }
        return null;
    }
}
