package ru.reboot.organizer.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "platform_accounts", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"platform", "platform_user_id"})
})
public class PlatformAccount
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser appUser;

    @Column(name = "platform", nullable = false)
    @Enumerated(EnumType.STRING)
    private PlatformType platformType;

    @Column(name = "platform_user_id", nullable = false)
    private String platformUserId;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public enum PlatformType {
        tg,
        max
    }
}