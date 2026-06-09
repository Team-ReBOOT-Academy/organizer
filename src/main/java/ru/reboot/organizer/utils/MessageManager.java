package ru.reboot.organizer.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageManager {
    private final MessageSource messageSource;
    private final Locale defaultLocale = Locale.forLanguageTag("ru-RU");

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, code, defaultLocale);
    }

    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, code, defaultLocale);
    }
}
