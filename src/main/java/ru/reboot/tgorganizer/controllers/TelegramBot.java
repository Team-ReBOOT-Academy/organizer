package ru.reboot.tgorganizer.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Component
public class TelegramBot extends TelegramBotsLongPollingApplication {
    private final String botToken;
    private final String botName;

    public TelegramBot(
            @Value("${bot.token}") String botToken,
            @Value("${bot.name}") String botName) {

        this.botToken = botToken; // Сохраняем в поле нашего класса
        this.botName = botName;
    }
}
