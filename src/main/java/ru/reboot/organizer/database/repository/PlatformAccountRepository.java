package ru.reboot.organizer.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.reboot.organizer.database.entity.PlatformAccount;

import java.util.Optional;

@Repository
public interface PlatformAccountRepository extends JpaRepository<PlatformAccount, Long> {
    //можно убрать (если все хорошо работает с новым поиском, то убери вот этот старый)
    Optional<PlatformAccount> findByPlatformUserId(String platformUserId);
    //поиск по user и платформе
    Optional<PlatformAccount> findByPlatformUserIdAndPlatformType(String platformUserId, PlatformAccount.PlatformType platformType);
}