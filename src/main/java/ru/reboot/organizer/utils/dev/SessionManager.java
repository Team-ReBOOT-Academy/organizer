package ru.reboot.organizer.utils.dev;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    private final Map<Long, String> userScreenByUserId = new ConcurrentHashMap<>();
    private final Map<Long, String> userPlatforms = new ConcurrentHashMap<>();

    public void setUserScreen(Long userId, String screen) {
        userScreenByUserId.put(userId, screen);
    }

    public String getUserScreen(Long userId) {
        return userScreenByUserId.getOrDefault(userId, UserScreens.DEFAULT_SCREEN);
    }

    public void setUserPlatform(Long userId, String platform) {
        userPlatforms.put(userId, platform);
    }

    public String getUserPlatform(Long userId) {
        return userPlatforms.getOrDefault(userId, "TELEGRAM");
    }
}
