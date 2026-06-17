package ru.reboot.organizer.routing.handlers.screens.task.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.dto.UserScreens;
import ru.reboot.organizer.routing.ScreenHandler;
import ru.reboot.organizer.services.SessionManagerService;
import ru.reboot.organizer.utils.MessageManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

@Component
@RequiredArgsConstructor
public class NewTaskInputDeadlineScreenHandler implements ScreenHandler {
    private final SessionManagerService sessionManagerService;
    private final MessageManager messageManager;

    private final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("dd.MM.yyyy")

            .optionalStart()
            .appendPattern(" HH:mm")
            .optionalEnd()

            .optionalStart()
            .appendPattern(" H:mm")
            .optionalEnd()

            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .toFormatter();

    @Override
    public String getHandledScreen() {
        return UserScreens.NEW_TASK_INPUT_DEADLINE;
    }

    @Override
    public UnifiedResponse handleText(Long userId, String text) {
        try {
            LocalDateTime deadline = LocalDateTime.parse(text.trim(), formatter);

            if (deadline.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Ошибка, дедлайн не может быть раньше текущего времени");
            }

            SessionManagerService.TaskDraft draft = sessionManagerService.getOrCreateDraft(userId);
            draft.setDeadline(deadline);

            sessionManagerService.setUserScreen(userId, UserScreens.NEW_TASK_MARK_IMPORTANT);

            return UnifiedResponse.builder()
                    .text(messageManager.getMessage("task.new.important"))
                    .row()
                    .button(messageManager.getMessage("button.task.new.important.yes"), ButtonType.NEW_TASK_IMPORTANT_YES.getPayload())
                    .button(messageManager.getMessage("button.task.new.important.no"), ButtonType.NEW_TASK_IMPORTANT_NO.getPayload())
                    .row().button(messageManager.getMessage("button.task.new.cancel"), ButtonType.MAIN_MENU.getPayload())
                    .build();
        } catch (DateTimeParseException e) {
            return UnifiedResponse.builder()
                    .text(messageManager.getMessage("error.task.new.deadline.format") + "\n\n" + messageManager.getMessage("task.new.deadline"))
                    .row().button(messageManager.getMessage("button.task.new.deadline.skip"), ButtonType.NEW_TASK_SKIP_DEADLINE.getPayload())
                    .row().button(messageManager.getMessage("button.task.new.cancel"), ButtonType.MAIN_MENU.getPayload())
                    .build();
        } catch (IllegalArgumentException e) {
            return UnifiedResponse.builder()
                    .text(messageManager.getMessage("error.task.new.deadline.date") + "\n\n" + messageManager.getMessage("task.new.deadline"))
                    .row().button(messageManager.getMessage("button.task.new.deadline.skip"), ButtonType.NEW_TASK_SKIP_DEADLINE.getPayload())
                    .row().button(messageManager.getMessage("button.task.new.cancel"), ButtonType.MAIN_MENU.getPayload())
                    .build();
        }
    }
}
