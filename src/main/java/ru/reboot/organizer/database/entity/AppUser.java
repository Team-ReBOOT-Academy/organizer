package ru.reboot.organizer.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.reboot.organizer.dto.UserScreens;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "app_users")
public class AppUser
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identification_code")
    private String phoneNumber;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @Column(name = "current_screen")
    private String currentScreen = UserScreens.DEFAULT_SCREEN;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_active_platform")
    @Enumerated(EnumType.STRING)
    private PlatformAccount.PlatformType lastActivePlatform = PlatformAccount.PlatformType.TELEGRAM;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlatformAccount> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();
}
