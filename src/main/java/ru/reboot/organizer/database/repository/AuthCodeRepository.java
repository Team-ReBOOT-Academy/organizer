package ru.reboot.organizer.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.reboot.organizer.database.entity.AuthCode;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AuthCodeRepository extends JpaRepository<AuthCode, Long> {
    Optional<AuthCode> findByCodeAndIsUsedFalseAndExpiresAtAfter(String code, LocalDateTime now);
}