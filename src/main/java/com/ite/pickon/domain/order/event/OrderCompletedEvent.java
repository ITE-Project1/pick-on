package com.ite.pickon.domain.order.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCompletedEvent {
    private final String userPhoneNumber;
    private final String message;
}
