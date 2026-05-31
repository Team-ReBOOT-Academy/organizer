package ru.reboot.organizer.routing.handlers.screen;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.routing.ScreenHandler;
import ru.reboot.organizer.services.PlatformLinkService;
import ru.reboot.organizer.services.outbound.NotificationService;
import ru.reboot.organizer.utils.dev.SessionManager;
import ru.reboot.organizer.utils.dev.UserScreens;

@Component
@RequiredArgsConstructor
public class PlatformLinkInputScreenHandler implements ScreenHandler {
    private final PlatformLinkService platformLinkService;
    private final NotificationService notificationService;
    private final SessionManager sessionManager;

    @Override
    public String getHandledScreen() {
        return UserScreens.PLATFORM_LINK_INPUT;
    }

    @Override
    public UnifiedResponse handleText(Long userId, String text) {
        String cleanCode = text.trim().toUpperCase();

        PlatformLinkService.LinkAttemptResult attempt = platformLinkService.linkAccountByCode(userId, cleanCode);

        sessionManager.setUserScreen(userId, UserScreens.MAIN_MENU);

        return switch (attempt.result()) {
            case PENDING_CONFIRMATION -> {
                UnifiedResponse pushMessage = UnifiedResponse.builder()
                        .text("Попытка входа с другого устройства. Разрешить объединение профилей?")
                        .row()
                        .button("Подтвердить", ButtonType.CONFIRM_LINK)
                        .button("Отклонить", ButtonType.REJECT_LINK)
                        .build();

                notificationService.sendNotification(attempt.creatorUserId(), pushMessage);

                yield UnifiedResponse.builder()
                        .text("Код верный\n\nНа платформу, где был сгенерирован код, отправлен запрос. Подтвердите подключение там.")
                        .row().button("В главное меню", ButtonType.MAIN_MENU)
                        .build();
            }

            case INVALID -> UnifiedResponse.builder()
                    .text("Неверный код. Проверьте правильность ввода.")
                    .row().button("Попробовать еще раз", ButtonType.ENTER_LINK_CODE)
                    .row().button("В главное меню", ButtonType.MAIN_MENU)
                    .build();

            case EXPIRED -> UnifiedResponse.builder()
                    .text("Время действия кода истекло (прошло больше 10 минут). Сгенерируйте новый.")
                    .row().button("В главное меню", ButtonType.MAIN_MENU)
                    .build();

            case SAME_USER -> UnifiedResponse.builder()
                    .text("Вы пытаетесь привязать платформу саму к себе :)")
                    .row().button("В главное меню", ButtonType.MAIN_MENU)
                    .build();

            case SUCCESS -> UnifiedResponse.builder()
                    .text("Платформа успешно привязана")
                    .row().button("В главное меню", ButtonType.MAIN_MENU)
                    .build();
        };
    }
}
