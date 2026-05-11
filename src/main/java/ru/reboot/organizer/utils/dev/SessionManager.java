package ru.reboot.organizer.utils.dev;

import org.springframework.stereotype.Component;
import ru.reboot.organizer.entities.UserState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    private final Map<Long, UserState> sessions = new ConcurrentHashMap<>();

    public UserState getUserState(Long chatId) {
        return sessions.getOrDefault(chatId, UserState.UNKNOWN_STATE);
    }

    public void setUserState(Long chatId, UserState state) {
        sessions.put(chatId, state);
    }
}
