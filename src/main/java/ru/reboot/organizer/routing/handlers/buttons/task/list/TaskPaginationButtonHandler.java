package ru.reboot.organizer.routing.handlers.buttons.task.list;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.database.entity.Task;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.services.TaskService;
import ru.reboot.organizer.utils.MessageManager;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskPaginationButtonHandler implements ButtonHandler {
    private final TaskService taskService;
    private final MessageManager messageManager;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.TASK_LIST_PAGE_TASKS;
    }

    @Override
    public UnifiedResponse handleButton(Long userId, String payload) {
        String data = payload.replace(ButtonType.TASK_LIST_PAGE_TASKS.getPayload(), "");
        String[] parts = data.split("_");
        String category = parts[0];
        int page = Integer.parseInt(parts[1]);

        Page<Task> tasksPage = null;
        String headerMessageKey = "";
        String emptyMessageKey = "";

        switch (category) {
            case "imp" -> {
                tasksPage = taskService.getImportantTasks(userId, page);
                headerMessageKey = "task.list.category.important.header";
                emptyMessageKey = "task.list.category.important.empty";
            }
            case "cmp" -> {
                tasksPage = taskService.getCompletedTasks(userId, page);
                headerMessageKey = "task.list.category.completed.header";
                emptyMessageKey = "task.list.category.completed.empty";
            }
            case "oth" -> {
                tasksPage = taskService.getOtherTasks(userId, page);
                headerMessageKey = "task.list.category.others.header";
                emptyMessageKey = "task.list.category.others.empty";
            }
        }

        if (tasksPage == null || tasksPage.isEmpty()) {
            return UnifiedResponse.builder()
                    .text(messageManager.getMessage(emptyMessageKey))
                    .row().button(messageManager.getMessage("button.task.list.categories"), ButtonType.TASK_LIST_CATEGORIES.getPayload())
                    .build();
        }

        String headerText = messageManager.getMessage(headerMessageKey, page + 1, tasksPage.getTotalPages());

        List<Task> tasks = tasksPage.getContent();

        UnifiedResponse.UnifiedResponseBuilder responseBuilder = UnifiedResponse.builder()
                .text(headerText);

        for (Task task : tasks) {
            String buttonText = task.getTitle();

            responseBuilder.row().button(buttonText, ButtonType.TASK_LIST_VIEW_TASK.getPayload() + task.getId());
        }

        responseBuilder.row();
        if (tasksPage.hasPrevious()) {
            responseBuilder.button(messageManager.getMessage("button.task.list.prev"), ButtonType.TASK_LIST_PAGE_TASKS.getPayload() + category + "_" + (page - 1));
        }
        if (tasksPage.hasNext()) {
            responseBuilder.button(messageManager.getMessage("button.task.list.next"), ButtonType.TASK_LIST_PAGE_TASKS.getPayload() + category + "_" + (page + 1));
        }

        responseBuilder.row().button(messageManager.getMessage("button.task.list.categories"), ButtonType.TASK_LIST_CATEGORIES.getPayload());

        return responseBuilder.build();
    }
}
