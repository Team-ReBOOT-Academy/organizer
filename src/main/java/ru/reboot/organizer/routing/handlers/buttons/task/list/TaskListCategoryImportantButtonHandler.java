package ru.reboot.organizer.routing.handlers.buttons.task.list;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.dto.UserScreens;
import ru.reboot.organizer.routing.ButtonHandler;
import ru.reboot.organizer.services.SessionManagerService;

@Component
@RequiredArgsConstructor
public class TaskListCategoryImportantButtonHandler implements ButtonHandler {
    private final SessionManagerService sessionManagerService;
    private final TaskPaginationButtonHandler taskPaginationButtonHandler;

    @Override
    public ButtonType getHandledButton() {
        return ButtonType.TASK_LIST_CATEGORY_IMPORTANT;
    }

    @Override
    public UnifiedResponse handleButton(Long userId, String payload) {
        sessionManagerService.setUserScreen(userId, UserScreens.TASK_LIST_CATEGORY_IMPORTANT);

        String paginationPayload = ButtonType.TASK_LIST_PAGE_TASKS.getPayload() + "imp_0";
        return taskPaginationButtonHandler.handleButton(userId, paginationPayload);
    }
}
