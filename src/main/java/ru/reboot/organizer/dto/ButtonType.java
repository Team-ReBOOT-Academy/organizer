package ru.reboot.organizer.dto;

import lombok.Getter;

/**
 * Класс с перечислением всех возможных кнопок
 */

@Getter
public enum ButtonType {
    MAIN_MENU("btn_main_menu"),

    NEW_TASK("btn_new_task"),
    TASK_LIST("btn_task_list"),
    LINK_PLATFORM("btn_link_platform"),

    PAGE_TASKS("btn_page_tasks_"),
    VIEW_TASK("btn_view_task_"),
    TOGGLE_IMPORTANT("btn_important_"),
    COMPLETE_TASK("btn_complete_"),
    DELETE_TASK("btn_delete_"),

    GENERATE_LINK_CODE("btn_generate_link_code"),
    ENTER_LINK_CODE("btn_enter_link_code"),
    CONFIRM_LINK("btn_confirm_link"),
    REJECT_LINK("btn_reject_link");

    private final String payload;

    ButtonType(String payload) {
        this.payload = payload;
    }

    public static ButtonType fromPayload(String payload) {
        if (payload == null) return null;

        for(ButtonType button : ButtonType.values()) {
            if (button.payload.equals(payload)) {
                return button;
            }

            if (payload.startsWith(button.payload) && button.payload.endsWith("_")) {
                return button;
            }
        }

        return null;
    }

}
