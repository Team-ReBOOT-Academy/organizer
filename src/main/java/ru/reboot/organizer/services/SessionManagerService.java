package ru.reboot.organizer.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.reboot.organizer.database.entity.AppUser;
import ru.reboot.organizer.database.entity.PlatformAccount;
import ru.reboot.organizer.database.repository.AppUserRepository;
import ru.reboot.organizer.database.repository.PlatformAccountRepository;
import ru.reboot.organizer.dto.UserScreens;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionManagerService {
    private final AppUserRepository appUserRepository;
    private final PlatformAccountRepository platformAccountRepository;

    @Transactional
    public Long getOrCreateAppUserId(String platformUserId, PlatformAccount.PlatformType platformType) {
        return platformAccountRepository.findByPlatformUserId(platformUserId)
                .map(account -> account.getAppUser().getId())
                .orElseGet(() -> registerNewUser(platformUserId, platformType));
    }

    @Transactional
    public void setUserScreen(Long appUserId, String screen) {
        appUserRepository.findById(appUserId).ifPresent(user -> {
            user.setCurrentScreen(screen);
            appUserRepository.save(user); // Благодаря @Transactional изменения улетят в БД
        });
    }

    public String getUserScreen(Long appUserId) {
        return appUserRepository.findById(appUserId)
                .map(AppUser::getCurrentScreen)
                .orElse(UserScreens.DEFAULT_SCREEN);
    }

    @Transactional
    public void setUserPlatform(Long appUserId, PlatformAccount.PlatformType platform) {
        appUserRepository.findById(appUserId).ifPresent(user -> {
            user.setLastActivePlatform(platform);
            appUserRepository.save(user);
        });
    }

    public PlatformAccount.PlatformType getUserPlatform(Long appUserId) {
        return appUserRepository.findById(appUserId)
                .map(AppUser::getLastActivePlatform)
                .orElse(PlatformAccount.PlatformType.telegram);
    }

    private Long registerNewUser(String platformUserId, PlatformAccount.PlatformType platformType) {
        AppUser newUser = new AppUser();
        newUser.setCurrentScreen(UserScreens.DEFAULT_SCREEN);
        newUser.setCreatedAt(LocalDateTime.now());
        AppUser savedUser = appUserRepository.save(newUser);

        PlatformAccount account = new PlatformAccount();
        account.setAppUser(savedUser);
        account.setPlatformType(platformType);
        account.setPlatformUserId(platformUserId);
        account.setDeleted(false);
        platformAccountRepository.save(account);

        return savedUser.getId();
    }
}
