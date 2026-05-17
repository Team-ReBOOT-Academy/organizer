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
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.dto.UserRequest;
import ru.reboot.organizer.mappers.telegram.TelegramRequestMapper;
import ru.reboot.organizer.mappers.telegram.TelegramResponseMapper;
import ru.reboot.organizer.services.CoreRouterService;

/**
 * Класс-контроллер для Telegram бота
 */
@Slf4j
@Component
public class TelegramBotController implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final String botToken;
    private final TelegramRequestMapper requestMapper;
    private final TelegramResponseMapper responseMapper;
    private final CoreRouterService coreRouterService;

    public TelegramBotController(@Value("${bot.telegram.token}") String botToken,
                                 TelegramClient telegramClient,
                                 TelegramRequestMapper requestMapper,
                                 TelegramResponseMapper responseMapper,
                                 CoreRouterService coreRouterService
    ) {
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
        this.coreRouterService = coreRouterService;
        this.botToken = botToken;
        this.telegramClient = telegramClient;

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
        try {
            UserRequest request = requestMapper.map(update);

            if (request.globalUserId() == -1L || request.text().isEmpty()) return;

            UnifiedResponse unifiedResponse = coreRouterService.route(request);

            Integer messageIdToEdit = requestMapper.extractMessageIdForEdit(update);

            BotApiMethod<?> telegramResponse = responseMapper.map(request.globalUserId(), messageIdToEdit, unifiedResponse);

            telegramClient.execute(telegramResponse);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке Telegram запроса: {}", e.getMessage());
        }
    }
}
