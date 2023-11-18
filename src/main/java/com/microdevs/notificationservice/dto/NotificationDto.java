package com.microdevs.notificationservice.dto;

import com.microdevs.notificationservice.enums.MailType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class NotificationDto implements Serializable {
    private String to;
    private Map<String, String> map;
    private MailType subject;
}
