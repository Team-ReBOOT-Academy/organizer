package ru.reboot.organizer.dto;

/**
 * Класс-запрос от пользователя
 * @param globalUserId UserID внутри проекта
 * @param text текст, отправленный пользователем
 * @param platform платформа, с которой было отправлен request
 */

public record UserRequest(
        Long globalUserId,
        String text,
        String platform
) {}
