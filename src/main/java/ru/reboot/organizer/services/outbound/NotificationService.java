package ru.reboot.organizer.services.outbound;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.SSP55.max.bots.api.client.MaxClient;
import ru.SSP55.max.bots.api.exceptions.MaxApiException;
import ru.SSP55.max.bots.api.methods.post.sendmessage.SendMessage;
import ru.SSP55.max.bots.api.objects.newmessagebody.NewMessageBody;
import ru.reboot.organizer.database.entity.AppUser;
import ru.reboot.organizer.database.entity.PlatformAccount;
import ru.reboot.organizer.database.repository.AppUserRepository;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.mappers.max.MaxResponseMapper;
import ru.reboot.organizer.mappers.telegram.TelegramResponseMapper;

/**
 * Сервис отправки уведомлений пользователю
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final AppUserRepository appUserRepository;

    private final TelegramResponseMapper telegramResponseMapper;
    private final MaxResponseMapper maxResponseMapper;

    private final TelegramClient telegramClient;
    private final MaxClient maxClient;

    @Transactional(readOnly = true)
    public void sendNotification(Long globalUserId, UnifiedResponse notificationContent) {
        AppUser appUser = appUserRepository.findById(globalUserId).orElse(null);
        if (appUser == null) {
            log.warn("Не удалось отправить уведомление: пользователь с ID {} не найден.", globalUserId);
            return;
        }

        PlatformAccount.PlatformType targetPlatform = appUser.getLastActivePlatform();

        PlatformAccount targetAccount = appUser.getAccounts().stream()
                .filter(acc -> acc.getPlatformType() == targetPlatform && !acc.isDeleted())
                .findFirst()
                .orElse(null);

        if (targetAccount == null) {
            log.warn("Не удалось отправить уведомление: нет активного аккаунта для платформы {}", targetPlatform);
            return;
        }

        switch (targetPlatform) {
            case TELEGRAM:
                Long telegramChatId = Long.valueOf(targetAccount.getPlatformUserId());

                BotApiMethod<?> telegramMessage = telegramResponseMapper.map(telegramChatId, null, notificationContent);
                try {
                    telegramClient.execute(telegramMessage);
                } catch (TelegramApiException e) {
                    log.error("Ошибка при отправке уведомления в Telegram (User: {}): {}", telegramChatId, e.getMessage());
                }
                break;

            case MAX:
                Long maxUserId = Long.valueOf(targetAccount.getPlatformUserId());

                NewMessageBody messageBody = maxResponseMapper.mapToMaxMessage(notificationContent);
                SendMessage maxMessageRequest = SendMessage.builder()
                        .userId(maxUserId)
                        .body(messageBody)
                        .build();
                try {
                    maxClient.execute(maxMessageRequest);
                } catch (MaxApiException e) {
                    log.error("Ошибка при отправке уведомления в Max (User: {}): {}", maxUserId, e.getMessage());
                }
                break;

            default:
                log.error("Неизвестная платформа для отправки: {}", targetPlatform);
        }
    }
}
