package ru.reboot.organizer.routing;

import ru.reboot.organizer.dto.UnifiedResponse;

public interface ScreenHandler {
    String getHandledScreen();
    UnifiedResponse handleText(Long userId, String text);
}
