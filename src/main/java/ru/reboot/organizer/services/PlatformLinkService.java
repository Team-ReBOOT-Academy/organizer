package ru.reboot.organizer.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.reboot.organizer.database.entity.AppUser;
import ru.reboot.organizer.database.entity.AuthCode;
import ru.reboot.organizer.database.repository.AppUserRepository;
import ru.reboot.organizer.database.repository.AuthCodeRepository;
import ru.reboot.organizer.database.repository.PlatformAccountRepository;
import ru.reboot.organizer.database.repository.TaskRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Сервис для подключения платформ
 */

@Service
@RequiredArgsConstructor
public class PlatformLinkService {
    private final AuthCodeRepository authCodeRepository;
    private final AppUserRepository appUserRepository;
    private final PlatformAccountRepository platformAccountRepository;
    private final TaskRepository taskRepository;
    private final NotificationService notificationService;

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public String generateAndSaveCode(Long primaryUserId) {
        AppUser primaryUser = appUserRepository.findById(primaryUserId)
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));

        Optional<AuthCode> existingCode = authCodeRepository.findByAppUserAndIsUsedFalseAndExpiresAtAfter(
                primaryUser, LocalDateTime.now()
        );

        if (existingCode.isPresent()) {
            return existingCode.get().getCode();
        }

        String code = String.format("%06d", secureRandom.nextInt(1000000));
        AuthCode authCode = new AuthCode();
        authCode.setCode(code);
        authCode.setAppUser(primaryUser);
        authCode.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        authCodeRepository.save(authCode);

        return code;
    }

    @Transactional
    public Long validateCodeAndPrepareMerge(String code, Long secondaryUserId) {
        AuthCode authCode = authCodeRepository.findByCodeAndIsUsedFalseAndExpiresAtAfter(code, LocalDateTime.now())
                .orElseThrow(() -> new IllegalArgumentException("Код не найден, истек или уже был использован"));

        authCode.setUsed(true);
        authCodeRepository.save(authCode);

        AppUser primaryUser = authCode.getAppUser();

        if (primaryUser.getId().equals(secondaryUserId)) {
            throw new IllegalStateException("Вы пытаетесь связать аккаунт с самим собой");
        }

        notificationService.sendMergeConfirmationRequest(primaryUser.getId(), secondaryUserId);

        return primaryUser.getId();
    }

    @Transactional
    public void mergeAccounts(Long primaryUserID, Long secondaryUserId) {
        AppUser primaryUser = appUserRepository.findById(primaryUserID)
                .orElseThrow(() -> new IllegalStateException("Основной профиль не найден"));

        AppUser secondaryUser = appUserRepository.findById(secondaryUserId)
                .orElseThrow(() -> new IllegalStateException("Вторичный профиль не найден"));

        platformAccountRepository.reassignAllAccounts(primaryUser, secondaryUser);
        taskRepository.reassignAllTasks(primaryUser, secondaryUser);

        appUserRepository.delete(secondaryUser);
    }


}
