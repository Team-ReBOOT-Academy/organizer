package ru.reboot.organizer.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.SSP55.max.bots.api.client.MaxClient;
import ru.SSP55.max.bots.api.core.MaxBotUpdateListener;
import ru.SSP55.max.bots.api.exceptions.MaxApiException;
import ru.SSP55.max.bots.api.methods.post.sendmessage.SendMessage;
import ru.SSP55.max.bots.api.objects.newmessagebody.NewMessageBody;
import ru.SSP55.max.bots.api.objects.update.bot.BotStartedUpdate;
import ru.SSP55.max.bots.api.objects.update.message.MessageCallbackUpdate;
import ru.SSP55.max.bots.api.objects.update.message.MessageCreatedUpdate;
import ru.reboot.organizer.database.entity.PlatformAccount;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.dto.UserRequest;
import ru.reboot.organizer.mappers.max.MaxResponseMapper;
import ru.reboot.organizer.services.CoreRouterService;
import ru.reboot.organizer.services.SessionManagerService;

/**
 * Класс-контроллер для бота в MAX
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class MaxBotController implements MaxBotUpdateListener {
    private final MaxResponseMapper responseMapper;
    private final CoreRouterService coreRouterService;
    private final MaxClient maxClient;
    private final SessionManagerService sessionManagerService;

    @Override
    public void onBotStarted(BotStartedUpdate update) {
        Long userId = update.getUser().getUserId();
        processUpdate(userId, "/start");
    }

    @Override
    public void onMessageCreated(MessageCreatedUpdate update) {
        Long userId = update.getMessage().getSender().getUserId();
        String text = update.getMessage().getBody().text();
        processUpdate(userId, text);
    }

    @Override
    public void onMessageCallback(MessageCallbackUpdate update) {
        String payload = update.getCallback().payload();
        Long userId = update.getCallback().user().getUserId();
        processUpdate(userId, payload);
    }

    private void processUpdate(Long userId, String incomingData) {
        try {
            String maxUserId = String.valueOf(userId);

            Long globalAppUserId = sessionManagerService.getOrCreateAppUserId(maxUserId, PlatformAccount.PlatformType.MAX);
            sessionManagerService.setUserPlatform(globalAppUserId, PlatformAccount.PlatformType.MAX);

            String processedText = (incomingData == null || incomingData.isBlank()) ? "/start" : incomingData;

            UserRequest internalRequest = new UserRequest(
                    globalAppUserId,
                    maxUserId,
                    processedText,
                    PlatformAccount.PlatformType.MAX
            );

            UnifiedResponse unifiedResponse = coreRouterService.route(internalRequest);

            NewMessageBody messageBody = responseMapper.mapToMaxMessage(unifiedResponse);

            SendMessage message = SendMessage.builder()
                    .userId(Long.valueOf(internalRequest.platformUserId()))
                    .body(messageBody)
                    .build();

            maxClient.execute(message);
        } catch (MaxApiException e) {
            log.error("Ошибка MAX: {}", e.getMessage());
        }
    }
}
