package ru.reboot.organizer.services.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.reboot.organizer.database.entity.AppUser;
import ru.reboot.organizer.database.entity.Reminder;
import ru.reboot.organizer.database.entity.Task;
import ru.reboot.organizer.database.repository.ReminderRepository;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.services.NotificationService;
import ru.reboot.organizer.utils.MessageManager;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReminderScheduler {
    private final ReminderRepository reminderRepository;
    private final NotificationService notificationService;
    private final MessageManager messageManager;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void processReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Reminder> dueReminders = reminderRepository.findByIsSentFalseAndReminderTimeBefore(now);

        if (dueReminders.isEmpty()) {
            return;
        }

        for (Reminder reminder : dueReminders) {
            try {
                Task task = reminder.getTask();
                AppUser user = task.getAppUser();

                if (task.isCompleted()) {
                    reminder.setSent(true);
                    continue;
                }

                UnifiedResponse response = UnifiedResponse.builder()
                        .text(messageManager.getMessage("notification.reminder", task.getTitle()))
                        .row().button(messageManager.getMessage("button.task.view"), ButtonType.TASK_LIST_VIEW_TASK.getPayload() + task.getId())
                        .build();

                notificationService.sendNotification(user.getId(), response);

                reminder.setSent(true);
            } catch (Exception e) {
                log.error("Ошибка при обработке напоминания. ID: {}", reminder.getId(), e);
            }
        }

        reminderRepository.saveAll(dueReminders);
    }
}
