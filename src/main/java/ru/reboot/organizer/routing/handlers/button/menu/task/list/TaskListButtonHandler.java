package ru.reboot.organizer.routing.handlers.button.menu.task.list;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.reboot.organizer.dto.ButtonType;
import ru.reboot.organizer.dto.UnifiedResponse;
import ru.reboot.organizer.routing.ButtonHandler;

@Component
@RequiredArgsConstructor
public class TaskListButtonHandler implements ButtonHandler {
    @Override
    public ButtonType getHandledButton() {
        return ButtonType.TASK_LIST;
    }

    @Override
    public UnifiedResponse handleButton(Long userId) {
        return UnifiedResponse.builder().
                text("Список задач пока пуст")
                .row()
                .button("В главное меню", ButtonType.MAIN_MENU)
                .build();
    }
}
