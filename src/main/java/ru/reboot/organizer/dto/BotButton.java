package ru.reboot.organizer.dto;

/**
 * Класс inline-кнопки
 * @param text содержимое кнопки
 * @param action callback-параметр
 */

public record BotButton(String text, String action) {}
