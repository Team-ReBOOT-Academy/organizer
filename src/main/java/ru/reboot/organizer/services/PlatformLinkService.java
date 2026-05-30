package ru.reboot.organizer.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PlatformLinkService {
    private final String alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private final int codeLength = 6;
    private final int ttlMinutes = 10;

    private final SecureRandom random = new SecureRandom();

    // Заглушка
    private final Map<String, Long> mockDatabase = new ConcurrentHashMap<>();

    private final Map<Long, Long> pendingConfirmations = new ConcurrentHashMap<>();

    public String getOrGenerateCodeForUser(Long globalUserId) {
        for (Map.Entry<String, Long> entry : mockDatabase.entrySet()) {
            if (entry.getValue().equals(globalUserId)) {
                return entry.getKey();
            }
        }

        String newCode = generateRandomCode();
        //LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(ttlMinutes);

        // Заглушка
        mockDatabase.put(newCode, globalUserId);

        return newCode;
    }

    public boolean confirmLink(Long creatorUserId) {
        Long targetUserId = pendingConfirmations.remove(creatorUserId);
        if (targetUserId != null) {
            // Заглушка
            return true;
        }
        return false;
    }

    public void rejectLink(Long creatorUserId) {
        // Заглушка
        pendingConfirmations.remove(creatorUserId);
    }

    // Заглушка
    public LinkAttemptResult linkAccountByCode(Long currentGlobalUserId, String inputCode) {
        if (!mockDatabase.containsKey(inputCode)) {
            return new LinkAttemptResult(LinkResult.INVALID, null);
        }

        Long creatorUserId = mockDatabase.get(inputCode);

        if (creatorUserId.equals(currentGlobalUserId)) {
            return new LinkAttemptResult(LinkResult.SAME_USER, null);
        }

        mockDatabase.remove(inputCode);
        pendingConfirmations.put(creatorUserId, currentGlobalUserId);

        return new LinkAttemptResult(LinkResult.PENDING_CONFIRMATION, creatorUserId);
    }

    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    public enum LinkResult {
        PENDING_CONFIRMATION,
        SUCCESS,
        INVALID,
        EXPIRED,
        SAME_USER
    }

    // Заглушка
    public record LinkAttemptResult(LinkResult result, Long creatorUserId) {}
}
