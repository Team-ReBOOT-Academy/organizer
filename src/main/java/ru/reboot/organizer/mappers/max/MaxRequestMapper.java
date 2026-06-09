package ru.reboot.organizer.mappers.max;

import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.UserRequest;

/**
 * Маппер входящих сообщений для MAX
 */

@Component
public class MaxRequestMapper {
    public UserRequest map(Long maxUserId, String text) {
        String processedText = (text == null || text.isBlank()) ? "/start" : text;

        return new UserRequest(maxUserId, processedText, "MAX");
    }
}
