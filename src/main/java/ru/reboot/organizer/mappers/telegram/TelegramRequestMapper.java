package ru.reboot.organizer.mappers.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.reboot.organizer.database.entity.PlatformAccount;
import ru.reboot.organizer.dto.UserRequest;

/**
 * Маппер входящих сообщений для Telegram
 */

@Component
public class TelegramRequestMapper {
    public UserRequest map (Update update) {
        Long telegramChatId = -1L;
        String text = "";

        if (update.hasMessage() && update.getMessage().hasText()) {
            telegramChatId = update.getMessage().getFrom().getId();
            text = update.getMessage().getText();
        } else if (update.hasCallbackQuery()) {
            telegramChatId = update.getCallbackQuery().getFrom().getId();
            text = update.getCallbackQuery().getData();
        }

        // Для Telegram chatId у UserRequest является platformUserId
        return new UserRequest(null, String.valueOf(telegramChatId), text, PlatformAccount.PlatformType.TELEGRAM);
    }

    public Integer extractMessageIdForEdit(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getMessageId();
        }
        return null;
    }
}
