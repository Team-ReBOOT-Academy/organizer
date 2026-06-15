package ru.reboot.organizer.dto;

import ru.reboot.organizer.database.entity.PlatformAccount;

/**
 * Класс-запрос от пользователя
 * @param globalUserId UserID внутри проекта
 * @param platformUserId UserID внутри платформы
 * @param text текст, отправленный пользователем
 * @param platform платформа, с которой было отправлен request
 */

public record UserRequest(
        Long globalUserId,
        String platformUserId,
        String text,
        PlatformAccount.PlatformType platform
) {}
