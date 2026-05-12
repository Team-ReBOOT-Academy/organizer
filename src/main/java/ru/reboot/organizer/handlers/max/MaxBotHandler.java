package ru.reboot.organizer.handlers.max;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.SSP55.max.bots.api.client.MaxClient;
import ru.SSP55.max.bots.api.core.MaxBotUpdateListener;
import ru.SSP55.max.bots.api.exceptions.MaxApiException;
import ru.SSP55.max.bots.api.objects.newmessagebody.NewMessageBody;
import ru.SSP55.max.bots.api.objects.update.message.MessageCreatedUpdate;
import ru.SSP55.max.bots.api.methods.post.sendmessage.SendMessage;

@Slf4j
@Component
public class MaxBotHandler implements MaxBotUpdateListener {
    private final MaxClient maxClient;

    public MaxBotHandler(MaxClient maxClient) {
        this.maxClient = maxClient;
    }

    @Override
    public void onMessageCreated(MessageCreatedUpdate update) {
        Long userId = update.getMessage().getSender().getUserId();
        String incomingText = update.getMessage().getBody().text();
        String senderFirstName = update.getMessage().getSender().getFirstName();

        log.info("Получено сообщение в MAX: {} от {}. User ID: {}", incomingText, senderFirstName, userId);

        SendMessage message = SendMessage.builder()
                .userId(userId)
                .body(NewMessageBody.builder().
                        text("Получено сообщение: " + incomingText)
                        .build())
                .build();

        try {
            maxClient.execute(message);
        } catch (MaxApiException e) {
            log.error("Ошибка: {}", e.getMessage());
        }
    }
}
