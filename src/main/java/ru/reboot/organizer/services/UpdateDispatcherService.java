package ru.reboot.organizer.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.reboot.organizer.utils.dev.SessionManager;
import ru.reboot.organizer.utils.keyboard.KeyboardFactory;

/**
 * Сервис определения и обработки Update
 */
@Service
@RequiredArgsConstructor
public class UpdateDispatcherService {
    private final SessionManager sessionManager;
    private final KeyboardFactory keyboardFactory;

    /**
     * Логика определения конкретного типа update из общего
     * @param update сырое обновление, полученное от сервера
     * @return Сообщение, отправляемое пользователю
     */
    public BotApiMethod<?> dispatch(Update update) {

        return null;
    }

    /**
     * Обработка Update типа Callback
     * @param callbackQuery событие нажатия на кнопку
     * @return сообщение для отправки пользователю
     */
    private BotApiMethod<?> processCallback(CallbackQuery callbackQuery) {
        // TODO
        return null;
    }

    /**
     * Обработка Update типа Text
     * @param message сообщение пользователя
     * @return сообщение для отправки пользователю
     */
    private BotApiMethod<?> processText(Message message) {
        // TODO
        return null;
    }
}
