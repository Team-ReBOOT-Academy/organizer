package ru.reboot.organizer.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.reboot.organizer.entities.UserState;
import ru.reboot.organizer.utils.SessionManager;
import ru.reboot.organizer.utils.KeyboardFactory;

@Service
@RequiredArgsConstructor
public class UpdateDispatcherService {
    private final SessionManager sessionManager;
    private final KeyboardFactory keyboardFactory;

    public BotApiMethod<?> dispatch(Update update) {
        if (update.hasCallbackQuery()) {
            return processCallback(update.getCallbackQuery());
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            return processText(update.getMessage());
        }

        return null;
    }

    private BotApiMethod<?> processCallback(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId();
        String data = callbackQuery.getData();

        switch (data) {
            case "CREATE_NOTE_BUTTON" -> {
                sessionManager.setUserState(chatId, UserState.WAITING_FOR_NOTE_TEXT);

                return EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageId) // Указываем ID сообщения, которое редактируем
                        .text("Введите текст заметки: ")
                        .replyMarkup(keyboardFactory.getBackToMenuKeyboard()) // Даем возможность отменить
                        .build();
            }

            case "BACK_TO_MAIN_MENU_BUTTON" -> {
                sessionManager.setUserState(chatId, UserState.WAITING_FOR_COMMAND);

                return EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .text("Главное меню:")
                        .replyMarkup(keyboardFactory.getMainMenuKeyboard())
                        .build();
            }

        }
        return null;
    }

    private BotApiMethod<?> processText(Message message) {
        long chatId = message.getChatId();
        String text = message.getText();
        UserState currentState = sessionManager.getUserState(chatId);

        if (text.equals("/start")) {
            sessionManager.setUserState(chatId, UserState.WAITING_FOR_COMMAND);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Выберите пункт")
                    .replyMarkup(keyboardFactory.getMainMenuKeyboard())
                    .build();
        }

        switch (currentState) {
            case WAITING_FOR_NOTE_TEXT -> {
                sessionManager.setUserState(chatId, UserState.WAITING_FOR_COMMAND);

                return SendMessage.builder()
                        .chatId(chatId)
                        .text("Заметка сохранена")
                        .build();
            }

            case WAITING_FOR_NOTE_ACTION -> {
                sessionManager.setUserState(chatId, UserState.CHOOSING_NOTE);

                return SendMessage.builder()
                        .chatId(chatId)
                        .text("Список заметок:")
                        .build();
            }
        }

        return null;
    }
}
