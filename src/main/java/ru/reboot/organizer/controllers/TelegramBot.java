package ru.reboot.organizer.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.reboot.organizer.services.UpdateDispatcherService;

/**
 * Класс-контроллер бота Telegram
 */
@Slf4j
@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final String botToken;
    private final UpdateDispatcherService service;

    public TelegramBot(@Value("${bot.telegram.token}") String botToken, TelegramClient telegramClient, UpdateDispatcherService service) {
        this.botToken = botToken;
        this.telegramClient = telegramClient;
        this.service = service;

        log.info("Инициализация ТГ бота прошла успешно");
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        BotApiMethod<?> response = service.dispatch(update);

        if (response != null) {
            try {
                telegramClient.execute(response);
            } catch (TelegramApiException e) {
                log.error("Ошибка при отправке сообщения: {}", e.getMessage());
            }
        }
    }
}
