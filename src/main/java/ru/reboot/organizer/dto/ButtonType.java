package ru.reboot.organizer.dto;

import lombok.Getter;

@Getter
public enum ButtonType {
    NEW_TASK("btn_new_task"),
    TASK_LIST("btn_task_list"),
    LINK_PLATFORM("btn_link_platform"),
    BACK_TO_MAIN_MENU("btn_back_to_main_menu"),
    GENERATE_LINK_CODE("btn_generate_link_code"),
    ENTER_LINK_CODE("btn_enter_link_code"),
    CONFIRM_LINK("btn_confirm_link"),
    REJECT_LINK("btn_reject_link");

    private final String payload;

    ButtonType(String payload) {
        this.payload = payload;
    }

    public static ButtonType fromPayload(String payload) {
        for(ButtonType button : ButtonType.values()) {
            if (button.payload.equals(payload)) {
                return button;
            }
        }

        return null;
    }

}
