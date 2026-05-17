package ru.reboot.organizer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.TelegramOkHttpClientFactory;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.reboot.organizer.utils.network.TelegramConnectionTester;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Настройки конфигурации для Telegram бота
 */
@Slf4j
@Configuration
public class TelegramBotConfig {
    @Bean(value = "okClient")
    public OkHttpClient okHttpClient(
            TelegramConnectionTester tester,
            @Value("${proxy.host}") String host,
            @Value("${proxy.port}") int port,
            @Value("${proxy.user}") String username,
            @Value("${proxy.pass}") String password
    ) {
        log.info("Инициализация HTTP-клиента для Telegram...");

        if (tester.isDirectConnectionAvailable()) {
            log.info("Будет использовано прямое подключение.");
            return new OkHttpClient();
        }

        log.warn("Прямое соединение недоступно. Попытка использовать прокси...");
        if (tester.isProxyConnectionAvailable(host, port, username, password)) {
            log.info("Будет использовано подключение через прокси.");

            return new TelegramOkHttpClientFactory.HttpProxyOkHttpClientCreator(
                    () -> new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port)),
                    () -> (route, response) -> {
                        String credentials = Credentials.basic(username, password);
                        return response
                                .request()
                                .newBuilder()
                                .header("Proxy-Authorization", credentials)
                                .build();
                    }
            ).get();
        }

        throw new IllegalStateException("Соединение невозможно ни одним способом");
    }

    @Bean(value = "telegramClient")
    public TelegramClient telegramClient(
            @Qualifier("okClient") OkHttpClient okClient,
            @Value("${bot.telegram.token}") String botToken
    ) {
        return new OkHttpTelegramClient(okClient, botToken);
    }

    @Bean(value = "telegramBotsApplication")
    public TelegramBotsLongPollingApplication telegramBotsApplication(
            @Qualifier("okClient") OkHttpClient okClient
    ) {
        return new TelegramBotsLongPollingApplication(ObjectMapper::new, () -> okClient);
    }
}
