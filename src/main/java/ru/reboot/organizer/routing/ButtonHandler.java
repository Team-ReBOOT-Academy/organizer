package ru.reboot.organizer.routing;

import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;

/**
 * Интерфейс-хендлер для экранов, где надо нажать на кнопку
 */

public interface ButtonHandler {
    ButtonType getHandledButton();
    UnifiedResponse handleButton(Long userId, String payload);
}
