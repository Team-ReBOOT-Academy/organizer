package ru.reboot.organizer;

import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@SpringBootTest(properties = {
        "bot.telegram.token=test_token",
        "proxy.host=",
        "proxy.port=0",
        "proxy.user=",
        "proxy.pass="
})
class TgOrganizerApplicationTests {
    @MockitoBean(name = "okClient")
    OkHttpClient okHttpClient;

    @MockitoBean(name = "telegramBotsApplication")
    TelegramBotsLongPollingApplication telegramBotsApplication;

    @Test
    void contextLoads() {
    }

}
