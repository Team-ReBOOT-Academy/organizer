package ru.reboot.organizer.dto;

import lombok.Getter;

/**
 * Класс с перечислением всех возможных кнопок
 */

@Getter
public enum ButtonType {
    // Статические кнопки
    MAIN_MENU("btn_main_menu"),

    NEW_TASK_INPUT_THEME("btn_new_task_input_theme"),
    NEW_TASK_INPUT_DESCRIPTION("btn_new_task_input_description"),
    NEW_TASK_SKIP_DESCRIPTION("btn_new_task_skip_description"),
    NEW_TASK_INPUT_DEADLINE("btn_new_task_input_deadline"),
    NEW_TASK_SKIP_DEADLINE("btn_new_task_skip_deadline"),
    NEW_TASK_IMPORTANT_YES("btn_new_task_important_yes"),
    NEW_TASK_IMPORTANT_NO("btn_new_task_important_no"),
    NEW_TASK_CANCEL("btn_new_task_cancel"),

    TASK_LIST_CATEGORIES("btn_task_list_categories"),
    TASK_LIST_CATEGORY_IMPORTANT("btn_task_list_category_important"),
    TASK_LIST_CATEGORY_OTHERS("btn_task_list_category_others"),
    TASK_LIST_CATEGORY_COMPLETED("btn_task_list_category_completed"),

    LINK_PLATFORM("btn_link_platform"),

    GENERATE_LINK_CODE("btn_generate_link_code"),
    ENTER_LINK_CODE("btn_enter_link_code"),
    CONFIRM_LINK("btn_confirm_link"),
    REJECT_LINK("btn_reject_link"),

    // Динамические кнопки
    TASK_LIST_PAGE_TASKS("btn_task_list_page_tasks_"),
    TASK_LIST_VIEW_TASK("btn_task_list_view_task_");

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
