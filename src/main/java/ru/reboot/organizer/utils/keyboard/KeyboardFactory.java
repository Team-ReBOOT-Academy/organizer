package ru.reboot.organizer.utils.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * Класс для генерации MarkUp у Telegram Inline-keyboard
 */
@Component
public class KeyboardFactory {
    public InlineKeyboardMarkup getMainMenuKeyboard() {
        return null;
    }

    public InlineKeyboardMarkup getBackToMenuKeyboard() {
        return null;
    }
}
