package ru.reboot.organizer.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PlatformLinkService {
    private final static String alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private final static int codeLength = 6;
    private final static int ttlMinutes = 10;

    private final static SecureRandom random = new SecureRandom();

    public static String getOrGenerateCodeForUser(Long globalUserId) {
        // TODO Проверка на активный код у пользователя

        String newCode = generateRandomCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(ttlMinutes);

        // TODO Сохранение в БД

        return newCode;
    }

    private static String generateRandomCode() {
        StringBuilder sb = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    public enum LinkResult {
        SUCCESS,
        INVALID,
        EXPIRED,
        SAME_USER
    }
}
