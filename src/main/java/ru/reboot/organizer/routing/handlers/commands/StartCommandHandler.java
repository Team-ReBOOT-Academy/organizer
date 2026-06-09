package ru.reboot.organizer.routing.handlers.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.dto.UserScreens;
import ru.reboot.organizer.routing.CommandHandler;
import ru.reboot.organizer.services.SessionManagerService;
import ru.reboot.organizer.utils.MessageManager;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements CommandHandler {
    private final MessageManager messageManager;
    private final SessionManagerService sessionManagerService;

    @Override
    public List<String> getHandledCommands() {
        return List.of("/start");
    }

    @Override
    public UnifiedResponse handleCommand(Long userId, String text) {
        sessionManagerService.setUserScreen(userId, UserScreens.DEFAULT_SCREEN);

        return UnifiedResponse.builder()
                .text(messageManager.getMessage("command.start"))
                .row().button(messageManager.getMessage("button.menu"), ButtonType.MAIN_MENU.getPayload())
                .build();
    }
}
