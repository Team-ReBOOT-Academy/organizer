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
import ru.reboot.organizer.database.entity.PlatformAccount;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.dto.UserRequest;
import ru.reboot.organizer.mappers.telegram.TelegramRequestMapper;
import ru.reboot.organizer.mappers.telegram.TelegramResponseMapper;
import ru.reboot.organizer.services.CoreRouterService;
import ru.reboot.organizer.services.SessionManagerService;

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
    private final SessionManagerService sessionManagerService;

    public TelegramBotController(@Value("${bot.telegram.token}") String botToken,
                                 TelegramClient telegramClient,
                                 TelegramRequestMapper requestMapper,
                                 TelegramResponseMapper responseMapper,
                                 CoreRouterService coreRouterService,
                                 SessionManagerService sessionManagerService
    ) {
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
        this.coreRouterService = coreRouterService;
        this.botToken = botToken;
        this.telegramClient = telegramClient;
        this.sessionManagerService = sessionManagerService;
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
            UserRequest rawRequest = requestMapper.map(update);
            if (rawRequest.platformUserId().equals("-1")) {
                return;
            }

            String telegramChatId = rawRequest.platformUserId();
            Long globalAppUserId = sessionManagerService.getOrCreateAppUserId(telegramChatId, PlatformAccount.PlatformType.telegram);
            sessionManagerService.setUserPlatform(globalAppUserId, PlatformAccount.PlatformType.telegram);

            UserRequest internalRequest = new UserRequest(
                    globalAppUserId,
                    telegramChatId,
                    rawRequest.text(),
                    PlatformAccount.PlatformType.telegram
            );

            UnifiedResponse unifiedResponse = coreRouterService.route(internalRequest);

            Integer messageIdToEdit = requestMapper.extractMessageIdForEdit(update);
            BotApiMethod<?> telegramResponse = responseMapper.map(Long.valueOf(internalRequest.platformUserId()), messageIdToEdit, unifiedResponse);
            telegramClient.execute(telegramResponse);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке Telegram запроса: {}", e.getMessage());
        }
    }
}
