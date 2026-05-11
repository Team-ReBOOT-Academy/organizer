package ru.reboot.organizer.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.reboot.organizer.utils.dev.SessionManager;
import ru.reboot.organizer.utils.dev.UserStates;
import ru.reboot.organizer.utils.keyboard.ButtonType;
import ru.reboot.organizer.utils.keyboard.KeyboardFactory;

@Slf4j
@Component
@RequiredArgsConstructor
public class ToMainMenuCommandHandler implements TelegramHandler {
    private final SessionManager sessionManager;
    private final KeyboardFactory keyboardFactory;

    @Override
    public Boolean supports(Update update, String currentState) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getData().equals(ButtonType.MAIN_MENU);
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            return update.getMessage().getText().equals("/menu");
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handle(Update update, String currentState, Long chatId) {
        sessionManager.setUserState(chatId, UserStates.MAIN_MENU);

        return SendMessage.builder()
                .chatId(chatId)
                .text("Главное меню: ")
                .replyMarkup(keyboardFactory.mainMenu())
                .build();
    }
}
