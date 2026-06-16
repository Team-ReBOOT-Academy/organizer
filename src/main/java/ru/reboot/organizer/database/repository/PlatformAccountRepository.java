package ru.reboot.organizer.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.reboot.organizer.database.entity.AppUser;
import ru.reboot.organizer.database.entity.PlatformAccount;

import java.util.Optional;

@Repository
public interface PlatformAccountRepository extends JpaRepository<PlatformAccount, Long> {
    //можно убрать (если все хорошо работает с новым поиском, то убери вот этот старый)
    Optional<PlatformAccount> findByPlatformUserId(String platformUserId);
    //поиск по user и платформе
    Optional<PlatformAccount> findByPlatformUserIdAndPlatformType(String platformUserId, PlatformAccount.PlatformType platformType);

    @Modifying
    @Query("UPDATE PlatformAccount p SET p.appUser = :primaryUser WHERE p.appUser = :secondaryUser")
    void reassignAllAccounts(@Param("primaryUser") AppUser primaryUser, @Param("secondaryUser") AppUser secondaryUser);
}