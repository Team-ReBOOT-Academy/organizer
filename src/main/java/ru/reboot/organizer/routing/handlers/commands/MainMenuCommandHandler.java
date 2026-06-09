package ru.reboot.organizer.routing.handlers.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.routing.CommandHandler;
import ru.reboot.organizer.routing.handlers.buttons.MainMenuButtonHandler;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MainMenuCommandHandler implements CommandHandler {
    private final MainMenuButtonHandler mainMenuButtonHandler;

    @Override
    public List<String> getHandledCommands() {
        return List.of("/menu");
    }

    @Override
    public UnifiedResponse handleCommand(Long userId, String text) {
        return mainMenuButtonHandler.handleButton(userId, text);
    }
}
