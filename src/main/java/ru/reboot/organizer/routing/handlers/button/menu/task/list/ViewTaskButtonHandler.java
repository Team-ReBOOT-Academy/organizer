package ru.reboot.organizer.routing.handlers.button.menu.task.list;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.utils.dev.MockTaskService;

@Component
@RequiredArgsConstructor
public class ViewTaskButtonHandler implements ButtonHandler {
    private final MockTaskService mockTaskService;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.VIEW_TASK;
    }

    @Override
    public UnifiedResponse handleButton(Long userId, String payload) {
        Long taskId = Long.parseLong(payload.replace(ButtonType.VIEW_TASK.getPayload(), ""));
        MockTaskService.MockTask task = mockTaskService.getTaskById(userId, taskId);

        if (task == null) {
            return UnifiedResponse.builder()
                    .text("Задача не найдена или была удалена")
                    .row().button("К списку задач", ButtonType.TASK_LIST)
                    .build();
        }

        String text = (task.isImportant() ? "🔥 **ВАЖНАЯ ЗАДАЧА**\n\n" : "**ЗАДАЧА**\n\n") +
                "**Тема:** " + task.getTitle() + "\n" +
                "**Описание:** " + task.getDescription() + "\n\n" +
                "Статус: " + (task.isCompleted() ? "✅ Выполнена" : "⏳ В процессе");

        return UnifiedResponse.builder()
                .text(text)
                .row()
                .button(task.isImportant() ? "Сделать неважной" : "Сделать важной", ButtonType.TOGGLE_IMPORTANT.getPayload() + taskId)
                .row()
                .button(task.isCompleted() ? "Возобновить" : "✅ Завершить", ButtonType.COMPLETE_TASK.getPayload() + taskId)
                .button("Удалить", ButtonType.DELETE_TASK.getPayload() + taskId)
                .row()
                .button("К списку", ButtonType.TASK_LIST)
                .build();
    }
}
