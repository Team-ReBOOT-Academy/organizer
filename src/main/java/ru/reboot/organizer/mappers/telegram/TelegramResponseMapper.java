package ru.reboot.organizer.mappers.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import ru.reboot.organizer.dto.BotButton;
import ru.reboot.organizer.dto.UnifiedResponse;

import java.util.List;

/**
 * Маппер исходящих сообщений для Telegram
 */

@Component
public class TelegramResponseMapper {
    public BotApiMethod<?> map(Long chatId, Integer messageId, UnifiedResponse response) {
        InlineKeyboardMarkup keyboard = buildTelegramKeyboard(response.getInlineKeyboard());

        if (messageId != null) {
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text(response.getText())
                    .replyMarkup(keyboard)
                    .parseMode("Markdown")
                    .build();
        }

        return SendMessage.builder()
                .chatId(chatId)
                .text(response.getText())
                .replyMarkup(keyboard)
                .parseMode("Markdown")
                .build();
    }

    private InlineKeyboardMarkup buildTelegramKeyboard(List<List<BotButton>> abstractKeyboard) {
        if (abstractKeyboard == null || abstractKeyboard.isEmpty()) {
            return null;
        }

        List<InlineKeyboardRow> tgRows = abstractKeyboard.stream()
                .map(row -> {
                    InlineKeyboardRow tgRow = new InlineKeyboardRow();

                    row.forEach(btn -> tgRow.add(
                            InlineKeyboardButton.builder()
                                    .text(btn.text())
                                    .callbackData(btn.action())
                                    .build()
                    ));

                    return tgRow;
                })
                .toList();

        return InlineKeyboardMarkup.builder().keyboard(tgRows).build();
    }
}
