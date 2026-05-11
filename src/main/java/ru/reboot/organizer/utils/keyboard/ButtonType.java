package ru.reboot.organizer.utils.keyboard;

import lombok.Getter;

/**
 * Класс типов кнопок Telegram Inline-keyboard
 */
@Getter
public enum ButtonType {
    UNKNOWN_BUTTON("UNKNOWN_BUTTON");

    private final String payload;

    ButtonType(String payload) {
        this.payload = payload;
    }

}
