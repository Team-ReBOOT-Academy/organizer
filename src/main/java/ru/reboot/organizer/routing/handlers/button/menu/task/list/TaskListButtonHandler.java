package ru.reboot.organizer.routing.handlers.button.menu.task.list;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.utils.dev.MockTaskService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskListButtonHandler implements ButtonHandler {
    private final MockTaskService mockTaskService;
    private final int PAGE_SIZE = 5;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.TASK_LIST;
    }

    @Override
    public UnifiedResponse handleButton(Long userId, String payload) {
        int currentPage = 0;
        if (payload.startsWith(ButtonType.PAGE_TASKS.getPayload())) {
            currentPage = Integer.parseInt(payload.replace(ButtonType.PAGE_TASKS.getPayload(), ""));
        }

        List<MockTaskService.MockTask> allTasks = mockTaskService.getTasksForUser(userId);

        if (allTasks.isEmpty()) {
            return UnifiedResponse.builder()
                    .text("Список задач пуст")
                    .row().button("Создать", ButtonType.NEW_TASK)
                    .row().button("В главное меню", ButtonType.MAIN_MENU)
                    .build();
        }

        int totalPages = (int) Math.ceil((double) allTasks.size() / PAGE_SIZE);
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, allTasks.size());
        List<MockTaskService.MockTask> pageTasks = allTasks.subList(start, end);

        UnifiedResponse.UnifiedResponseBuilder builder = UnifiedResponse.builder()
                .text("Ваши задачи (Страница " + (currentPage + 1) + " из " + totalPages + "):");

        for (MockTaskService.MockTask task : pageTasks) {
            String icon = task.isCompleted() ? "✅ " : (task.isImportant() ? "🔥 " : "📄 ");
            builder.row().button(icon + task.getTitle(), ButtonType.VIEW_TASK.getPayload() + task.getId());
        }

        UnifiedResponse.UnifiedResponseBuilder paginationRow = builder.row();
        if (currentPage > 0) {
            paginationRow.button("⬅️ Назад", ButtonType.PAGE_TASKS.getPayload() + (currentPage - 1));
        }
        if (currentPage < totalPages - 1) {
            paginationRow.button("Вперед ➡️", ButtonType.PAGE_TASKS.getPayload() + (currentPage + 1));
        }

        builder.row().button("В главное меню", ButtonType.MAIN_MENU);
        return builder.build();
    }
}
