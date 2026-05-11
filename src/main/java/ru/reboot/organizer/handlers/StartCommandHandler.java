package ru.reboot.organizer.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.reboot.organizer.utils.dev.SessionManager;
import ru.reboot.organizer.utils.dev.UserStates;
import ru.reboot.organizer.utils.keyboard.KeyboardFactory;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommandHandler implements TelegramHandler {
    private final SessionManager sessionManager;
    private final KeyboardFactory keyboardFactory;

    @Override
    public Boolean supports(Update update, String currentState) {
        return update.hasMessage() &&
                update.getMessage().hasText() &&
                update.getMessage().getText().equals("/start") &&
                currentState.equals(UserStates.DEFAULT_STATE);
    }

    @Override
    public BotApiMethod<?> handle(Update update, String currentState, Long chatId) {
        sessionManager.setUserState(chatId, UserStates.JUST_STARTED);

        return SendMessage.builder()
                .chatId(chatId)
                .text("Вы впервые запустили бота")
                .replyMarkup(keyboardFactory.firstStart())
                .build();
    }
}
