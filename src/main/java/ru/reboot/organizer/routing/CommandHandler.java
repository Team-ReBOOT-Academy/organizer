package ru.reboot.organizer.routing;

import ru.reboot.organizer.dto.UnifiedResponse;

import java.util.List;

public interface CommandHandler {
    List<String> getHandledCommands();
    UnifiedResponse handleCommand(Long userId, String text);
}
