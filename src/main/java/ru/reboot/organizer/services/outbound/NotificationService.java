package ru.reboot.organizer.services.outbound;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {
    public void sendConfirmationRequest(Long creatorUserId, Long targetUserId) {
        log.info("Запрос на подтверждение привязки");
        log.info("Отправка сообщения пользователю с ID: {}", creatorUserId);
        log.info("Текст: В ваш профиль пытаются войти с другого устройства (ID: {}). Разрешить?", targetUserId);
        log.info("Кнопки: btn_confirm_link | btn_reject_link");
    }
}
