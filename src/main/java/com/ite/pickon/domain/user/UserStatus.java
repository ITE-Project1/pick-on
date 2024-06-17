package com.ite.pickon.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE(0, "ACTIVE"),
    INACTIVE(1, "INACTIVE"),
    BLACK(2, "BLACK");

    private final int statusCode;
    private final String status;
}

