package com.microdevs.notificationservice.integration;

import com.microdevs.notificationservice.dto.NotificationDto;
import com.microdevs.notificationservice.enums.MailType;
import com.microdevs.notificationservice.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaNotificationListener {

    private final EmailService service;

    @KafkaListener(topics = "notification", groupId = "notify", containerFactory = "factory")
    public void handleNotificationRequest(NotificationDto dto) throws MessagingException {

        if (dto.getSubject().name().equals(MailType.REGISTER.name())) {
            service.sendRegisterMail(dto);
        }
    }
}
