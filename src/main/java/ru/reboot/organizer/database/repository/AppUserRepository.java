package ru.reboot.organizer.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.reboot.organizer.database.entity.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
}
