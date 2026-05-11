package ru.reboot.organizer.utils.network;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TelegramConnectionTester {
    private final String TEST_URL = "https://api.telegram.org";

    public boolean isDirectConnectionAvailable() {
        log.info("Проверка прямого соединения с Telegram API");

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(TEST_URL)
                .head()
                .build();

        try (Response response = client.newCall(request).execute()) {
            log.info("Прямое подключение доступно. HTTP статус: {}", response.code());
            return true;
        } catch (IOException e) {
            log.warn("Прямое подключение недоступно: {}", e.getMessage());
            return false;
        }

    }

    public boolean isProxyConnectionAvailable(String host, int port, String username, String password) {
        log.info("Проверка соедининения с Telegram API через прокси {}:{}", host, port);

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        Authenticator proxyAuthenticator = (route, response) -> {
            String credentials = Credentials.basic(username, password);
            return response.request().newBuilder()
                    .header("Proxy-Authorization", credentials)
                    .build();
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(proxy)
                .proxyAuthenticator(proxyAuthenticator)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(TEST_URL)
                .head()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() || response.code() == 404) {
                log.info("Соединение через прокси успешно установлено!");
                return true;
            } else if (response.code() == 407) {
                log.error("Ошибка 407: Прокси отклонил логин/пароль");
                return false;
            } else {
                log.error("Неизвестный ответ от прокси: HTTP {}", response.code());
                return false;
            }
        } catch (IOException e) {
            log.error("Ошибка сети при проверке прокси: {}", e.getMessage());
            return false;
        }
    }
}
