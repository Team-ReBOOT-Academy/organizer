package ru.reboot.organizer.mappers.max;

import org.springframework.stereotype.Component;
import ru.reboot.organizer.database.entity.PlatformAccount;
import ru.reboot.organizer.dto.UserRequest;

/**
 * Маппер входящих сообщений для MAX
 */

@Component
@Deprecated
public class MaxRequestMapper {
    public UserRequest map(Long maxUserId, String text) {
        String processedText = (text == null || text.isBlank()) ? "/start" : text;

        return new UserRequest(null, String.valueOf(maxUserId), processedText, PlatformAccount.PlatformType.max);
    }
}
