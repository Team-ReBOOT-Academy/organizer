package ru.reboot.organizer.dto;

public record UserRequest(
        Long globalUserId,
        String text,
        String platform
) {}
