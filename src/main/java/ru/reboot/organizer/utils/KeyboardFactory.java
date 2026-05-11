package ru.reboot.organizer.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
public class KeyboardFactory {
    public InlineKeyboardMarkup getMainMenuKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton.builder().text("Создать заметку").callbackData(ButtonType.CREATE_NOTE_BUTTON.toString()).build(),
                        InlineKeyboardButton.builder().text("Мои заметки").callbackData(ButtonType.VIEW_NOTES_BUTTON.toString()).build()
                ))
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton.builder().text("Связать с MAX").callbackData(ButtonType.LINK_MAX_BUTTON.toString()).build()
                )).build();
    }

    public InlineKeyboardMarkup getBackToMenuKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton.builder().text("Назад в меню").callbackData(ButtonType.BACK_TO_MAIN_MENU_BUTTON.toString()).build()
                )).build();
    }
}
