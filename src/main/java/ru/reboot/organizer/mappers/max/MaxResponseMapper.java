package ru.reboot.organizer.mappers.max;

import org.springframework.stereotype.Component;
import ru.SSP55.max.bots.api.objects.newmessagebody.NewMessageBody;
import ru.SSP55.max.bots.api.objects.newmessagebody.attachments.AttachmentRequest;
import ru.SSP55.max.bots.api.objects.newmessagebody.attachments.buttons.ButtonRequest;
import ru.SSP55.max.bots.api.objects.newmessagebody.attachments.buttons.CallbackButtonRequest;
import ru.SSP55.max.bots.api.objects.newmessagebody.attachments.payloads.InlineKeyboardAttachmentRequestPayload;
import ru.reboot.organizer.dto.UnifiedResponse;

import java.util.List;

@Component
public class MaxResponseMapper {
    public NewMessageBody mapToMaxMessage(UnifiedResponse response) {
        var builder = NewMessageBody.builder()
                .text(response.getText());

        if (response.getInlineKeyboard() != null && !response.getInlineKeyboard().isEmpty()) {
            List<List<ButtonRequest>> maxKeyboardRows = response.getInlineKeyboard().stream()
                    .map(row -> row.stream()
                            .map(btn -> (ButtonRequest) new CallbackButtonRequest(
                                    btn.text(),
                                    btn.action().getPayload()
                            )).toList()
                    ).toList();

            InlineKeyboardAttachmentRequestPayload payload = new InlineKeyboardAttachmentRequestPayload(maxKeyboardRows);

            AttachmentRequest keyboardAttachment = AttachmentRequest.inlineKeyboard(payload);

            builder.attachments(List.of(keyboardAttachment));
        }

        return builder.build();
    }
}
