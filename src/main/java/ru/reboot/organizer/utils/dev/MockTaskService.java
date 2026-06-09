package ru.reboot.organizer.utils.dev;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MockTaskService {
    private final Map<Long, List<MockTask>> userTasks = new ConcurrentHashMap<>();
    private long idCounter = 1;

    @Data
    @AllArgsConstructor
    public static class MockTask {
        private Long id;
        private String title;
        private String description;
        private boolean isImportant;
        private boolean isCompleted;
    }

    public List<MockTask> getTasksForUser(Long userId) {
        return userTasks.computeIfAbsent(userId, id -> {
            List<MockTask> tasks = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                tasks.add(new MockTask(idCounter++, "Тестовая задача #" + i, "Описание задачи " + i, i % 3 == 0, false));
            }
            return tasks;
        });
    }

    public MockTask getTaskById(Long userId, Long taskId) {
        return getTasksForUser(userId).stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElse(null);
    }
}
