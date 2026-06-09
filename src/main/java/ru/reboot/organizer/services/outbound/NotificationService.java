package ru.reboot.organizer.services.outbound;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.SSP55.max.bots.api.client.MaxClient;
import ru.SSP55.max.bots.api.exceptions.MaxApiException;
import ru.SSP55.max.bots.api.methods.post.sendmessage.SendMessage;
import ru.SSP55.max.bots.api.objects.newmessagebody.NewMessageBody;
import ru.reboot.organizer.database.entity.PlatformAccount;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.mappers.max.MaxResponseMapper;
import ru.reboot.organizer.mappers.telegram.TelegramResponseMapper;
import ru.reboot.organizer.services.SessionManagerService;

/**
 * Сервис отправки уведомлений пользователю
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SessionManagerService sessionManagerService;

    private final TelegramResponseMapper telegramResponseMapper;
    private final MaxResponseMapper maxResponseMapper;

    private final TelegramClient telegramClient;
    private final MaxClient maxClient;

    public void sendNotification(Long globalUserId, UnifiedResponse notificationContent) {
        PlatformAccount.PlatformType platform = sessionManagerService.getUserPlatform(globalUserId);

        try {
            if (PlatformAccount.PlatformType.telegram.equals(platform)) {
                BotApiMethod<?> telegramMessage = telegramResponseMapper.map(globalUserId, null, notificationContent);
                telegramClient.execute(telegramMessage);
            } else if (PlatformAccount.PlatformType.max.equals(platform)) {
                NewMessageBody messageBody = maxResponseMapper.mapToMaxMessage(notificationContent);
                SendMessage maxMessageRequest = SendMessage.builder()
                        .userId(globalUserId)
                        .body(messageBody)
                        .build();

                maxClient.execute(maxMessageRequest);
            }
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке уведомления в Telegram: {}", e.getMessage());
        } catch (MaxApiException e) {
            log.error("Ошибка при отправке уведомления в Max: {}", e.getMessage());
        }
    }
}
