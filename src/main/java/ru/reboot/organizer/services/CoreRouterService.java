package ru.reboot.organizer.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.dto.UserRequest;
import ru.reboot.organizer.services.outbound.NotificationService;
import ru.reboot.organizer.utils.dev.SessionManager;
import ru.reboot.organizer.utils.dev.UserScreens;

@Service
@RequiredArgsConstructor
public class CoreRouterService {
    private final SessionManager sessionManager;
    private final PlatformLinkService platformLinkService;
    private final NotificationService notificationService;

    public UnifiedResponse route(UserRequest request) {
        String currentScreen = sessionManager.getUserScreen(request.globalUserId());
        String text = request.text();

        if ("/start".equals(text) || "СТАРТ".equalsIgnoreCase(text)) {
            return buildMainMenu(request.globalUserId());
        }

        ButtonType button = ButtonType.fromPayload(text);
        if (button != null) {
            return handleButtonPress(request.globalUserId(), button);
        }

        return switch (currentScreen) {
            case UserScreens.NEW_TASK_INPUT -> handleNewTaskInput(request.globalUserId(), text);
            case UserScreens.PLATFORM_LINK_INPUT -> handlePlatformLinkInput(request.globalUserId(), text);
            default -> buildMainMenu(request.globalUserId());
        };
    }

    private UnifiedResponse handlePlatformLinkInput(Long userId, String text) {
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
                        .row().button("В главное меню", ButtonType.BACK_TO_MAIN_MENU)
                        .build();
            }

            case INVALID -> UnifiedResponse.builder()
                    .text("Неверный код. Проверьте правильность ввода.")
                    .row().button("Попробовать еще раз", ButtonType.ENTER_LINK_CODE)
                    .row().button("В главное меню", ButtonType.BACK_TO_MAIN_MENU)
                    .build();

            case EXPIRED -> UnifiedResponse.builder()
                    .text("Время действия кода истекло (прошло больше 10 минут). Сгенерируйте новый.")
                    .row().button("В главное меню", ButtonType.BACK_TO_MAIN_MENU)
                    .build();

            case SAME_USER -> UnifiedResponse.builder()
                    .text("Вы пытаетесь привязать платформу саму к себе :)")
                    .row().button("В главное меню", ButtonType.BACK_TO_MAIN_MENU)
                    .build();

            case SUCCESS -> buildMainMenu(userId);
        };
    }

    private UnifiedResponse handleNewTaskInput(Long userId, String text) {
        sessionManager.setUserScreen(userId, UserScreens.MAIN_MENU);

        return UnifiedResponse.builder()
                .text("Задача " + text + " сохранена.\nГлавное меню:")
                .row()
                    .button("Новая задача", ButtonType.NEW_TASK)
                    .button("Мои задачи", ButtonType.TASK_LIST)
                .row()
                    .button("Подключить платформу", ButtonType.LINK_PLATFORM)
                .build();
    }

    private UnifiedResponse handleButtonPress(Long userId, ButtonType button) {
        return switch (button) {
            case NEW_TASK -> {
                sessionManager.setUserScreen(userId, UserScreens.NEW_TASK_INPUT);

                yield UnifiedResponse.builder()
                        .text("Введите название задачи")
                        .row().button("В главное меню", ButtonType.BACK_TO_MAIN_MENU)
                        .build();
            }

            case BACK_TO_MAIN_MENU -> buildMainMenu(userId);

            case TASK_LIST -> UnifiedResponse.builder().
                    text("Список задач пока пуст")
                    .row()
                        .button("В главное меню", ButtonType.BACK_TO_MAIN_MENU)
                    .build();

            case LINK_PLATFORM -> {
                sessionManager.setUserScreen(userId, UserScreens.LINK_PLATFORM_MENU);

                yield UnifiedResponse.builder()
                        .text("Подключение платформы")
                        .row()
                            .button("Сгенерировать код", ButtonType.GENERATE_LINK_CODE)
                        .row()
                            .button("Ввести код", ButtonType.ENTER_LINK_CODE)
                        .row()
                            .button("В главное меню", ButtonType.BACK_TO_MAIN_MENU)
                        .build();
            }

            case GENERATE_LINK_CODE -> {
                String code = platformLinkService.getOrGenerateCodeForUser(userId);

                yield UnifiedResponse.builder()
                        .text("Код привязки:\n\n"
                                + code + "\n\n" +
                                "Код действует 10 минут. Введите его на другой платформе")
                        .row()
                            .button("В главное меню", ButtonType.BACK_TO_MAIN_MENU)
                        .build();
            }

            case ENTER_LINK_CODE -> {
                sessionManager.setUserScreen(userId, UserScreens.PLATFORM_LINK_INPUT);
                yield UnifiedResponse.builder()
                        .text("Введите 6-значный код привязки:")
                        .row()
                        .button("Отмена", ButtonType.BACK_TO_MAIN_MENU)
                        .build();
            }

            case CONFIRM_LINK -> {
                boolean isMerged = platformLinkService.confirmLink(userId);

                if (isMerged) {
                    yield UnifiedResponse.builder()
                            .text("✅ Платформы успешно связаны! Теперь у вас общая база задач.")
                            .row().button("В главное меню", ButtonType.BACK_TO_MAIN_MENU)
                            .build();
                } else {
                    yield UnifiedResponse.builder()
                            .text("❌ Заявка на привязку не найдена или устарела.")
                            .row().button("В главное меню", ButtonType.BACK_TO_MAIN_MENU)
                            .build();
                }
            }

            case REJECT_LINK -> {
                platformLinkService.rejectLink(userId);
                yield UnifiedResponse.builder()
                        .text("🛡 Подключение отклонено. Безопасность превыше всего.")
                        .row().button("В главное меню", ButtonType.BACK_TO_MAIN_MENU)
                        .build();
            }
        };
    }

    private UnifiedResponse buildMainMenu(Long userId) {
        sessionManager.setUserScreen(userId, UserScreens.MAIN_MENU);

        return UnifiedResponse.builder()
                .text("Главное меню")
                .row()
                    .button("Новая задача", ButtonType.NEW_TASK)
                    .button("Мои задачи", ButtonType.TASK_LIST)
                .row()
                    .button("Подключить платформу", ButtonType.LINK_PLATFORM)
                .build();
    }
}
