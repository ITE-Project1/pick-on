package com.ite.pickon.domain.transport;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransportStatus {

    PENDING(1, "PENDING"),
    SHIPPED(0, "SHIPPED"),
    COMPLETED(2, "COMPLETED");

    private final int statusCode;
    private final String status;

}
