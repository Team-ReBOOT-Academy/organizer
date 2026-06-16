package ru.reboot.organizer.routing.handlers.buttons.task.list;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.dto.UserScreens;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.services.SessionManagerService;
import ru.reboot.organizer.utils.MessageManager;

@Component
@RequiredArgsConstructor
public class TaskListCategoriesButtonHandler implements ButtonHandler {
    private final SessionManagerService sessionManagerService;
    private final MessageManager messageManager;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.TASK_LIST_CATEGORIES;
    }

    @Override
    public UnifiedResponse handleButton(Long userId, String payload) {
        sessionManagerService.setUserScreen(userId, UserScreens.TASK_LIST_CATEGORIES);

        return UnifiedResponse.builder()
                .text(messageManager.getMessage("task.list.categories"))
                .row().button(messageManager.getMessage("button.task.list.important"), ButtonType.TASK_LIST_CATEGORY_IMPORTANT.getPayload())
                .row().button(messageManager.getMessage("button.task.list.others"), ButtonType.TASK_LIST_CATEGORY_OTHERS.getPayload())
                .row().button(messageManager.getMessage("button.task.list.completed"), ButtonType.TASK_LIST_CATEGORY_COMPLETED.getPayload())
                .row().button(messageManager.getMessage("button.menu"), ButtonType.MAIN_MENU.getPayload())
                .build();
    }
}

