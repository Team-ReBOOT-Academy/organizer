package ru.reboot.organizer.utils.dev;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    private final Map<Long, String> userScreenByUserId = new ConcurrentHashMap<>();

    public void setUserScreen(Long userId, String screen) {
        userScreenByUserId.put(userId, screen);
    }

    public String getUserScreen(Long userId) {
        return userScreenByUserId.getOrDefault(userId, UserScreens.DEFAULT_SCREEN);
    }
}
