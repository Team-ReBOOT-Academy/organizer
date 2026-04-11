package ru.reboot.tgorganizer.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Component
public class Bot extends TelegramBotsLongPollingApplication {
    private final String botToken;
    private final String botName;

    public Bot(
            @Value("${bot.token}") String botToken,
            @Value("${bot.name}") String botName) {

        this.botToken = botToken; // Сохраняем в поле нашего класса
        this.botName = botName;
    }
}
