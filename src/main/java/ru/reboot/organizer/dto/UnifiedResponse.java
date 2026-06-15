package ru.reboot.organizer.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Унифицированный ответ пользователю
 */

@Data
public class UnifiedResponse {
    private final String text;
    private final List<List<BotButton>> inlineKeyboard;

    private UnifiedResponse(String text, List<List<BotButton>> inlineKeyboard) {
        this.text = text;
        this.inlineKeyboard = inlineKeyboard;
    }

    public static UnifiedResponseBuilder builder() {
        return new UnifiedResponseBuilder();
    }

    public static class UnifiedResponseBuilder {
        private String text;
        private final List<List<BotButton>> keyboard = new ArrayList<>();
        private List<BotButton> currentRow;

        public UnifiedResponseBuilder text(String text) {
            this.text = text;
            return this;
        }

        public UnifiedResponseBuilder row() {
            currentRow = new ArrayList<>();
            keyboard.add(currentRow);
            return this;
        }

        public UnifiedResponseBuilder button(String text, ButtonType action) {
            if (currentRow == null) {
                row();
            }
            currentRow.add(new BotButton(text, action.getPayload()));
            return this;
        }

        public UnifiedResponseBuilder button(String text, String payload) {
            if (currentRow == null) {
                row();
            }
            currentRow.add(new BotButton(text, payload));
            return this;
        }

        public UnifiedResponse build() {
            return new UnifiedResponse(text, keyboard);
        }
    }

}
