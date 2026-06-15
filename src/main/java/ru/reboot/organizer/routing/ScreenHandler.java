package ru.reboot.organizer.routing;

import ru.reboot.organizer.dto.UnifiedResponse;

/**
 * Интерфейс-хендлер для экранов, где надо ввести данные вручную
 */

public interface ScreenHandler {
    String getHandledScreen();
    UnifiedResponse handleText(Long userId, String text);
}
