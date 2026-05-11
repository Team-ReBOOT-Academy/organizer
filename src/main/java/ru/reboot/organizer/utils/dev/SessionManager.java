package ru.reboot.organizer.utils.dev;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    private final Map<Long, String> sessions = new ConcurrentHashMap<>();

    public String getUserState(Long chatId) {
        return sessions.getOrDefault(chatId, "DEFAULT_STATE");
    }

    public void setUserState(Long chatId, String state) {
        sessions.put(chatId, state);
    }
}
