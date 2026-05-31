package ru.reboot.organizer.routing;

import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;

public interface ButtonHandler {
    ButtonType getHandledButton();
    UnifiedResponse handleButton(Long userId);
}
