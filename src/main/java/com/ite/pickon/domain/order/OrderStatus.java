package com.ite.pickon.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING(2, "PENDING"),
    SHIPPED(0, "SHIPPED"),
    PICKUPREADY(1, "PICKUPREADY"),
    COMPLETED(3, "COMPLETED");

    private final int statusCode;
    private final String status;
}

