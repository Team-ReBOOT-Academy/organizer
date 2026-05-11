package ru.reboot.organizer.utils.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

/**
 * Класс для генерации MarkUp у Telegram Inline-keyboard
 */
@Component
public class KeyboardFactory {
    public InlineKeyboardMarkup firstStart() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton.builder().text("В главное меню").callbackData(ButtonType.MAIN_MENU).build()
                )).build();
    }

    public InlineKeyboardMarkup mainMenu() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton.builder().text("Мои заметки").callbackData(ButtonType.TASK_LIST).build(),
                        InlineKeyboardButton.builder().text("Новая заметка").callbackData(ButtonType.CREATE_NEW_TASK).build()
                ))
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton.builder().text("Связать с MAX").callbackData(ButtonType.LINK_MAX).build(),
                        InlineKeyboardButton.builder().text("О нас").callbackData(ButtonType.CREDENTIALS).build()
                )).build();
    }
}
