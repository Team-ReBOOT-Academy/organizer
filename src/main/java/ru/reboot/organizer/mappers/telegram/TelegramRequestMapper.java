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
        Long userId = -1L;
        String text = "";

        if (update.hasMessage() && update.getMessage().hasText()) {
            userId = update.getMessage().getFrom().getId();
            text = update.getMessage().getText();
        } else if (update.hasCallbackQuery()) {
            userId = update.getCallbackQuery().getFrom().getId();
            text = update.getCallbackQuery().getData();
        }

        return new UserRequest(null, String.valueOf(userId), text, PlatformAccount.PlatformType.telegram);
    }

    public Integer extractMessageIdForEdit(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getMessageId();
        }
        return null;
    }
}
