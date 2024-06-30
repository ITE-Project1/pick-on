package com.ite.pickon.domain.sms.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SmsSendEvent {
    private final String userPhoneNumber;
    private final String message;
}
