package com.ite.pickon.domain.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TransportScheduleVO {
    private int storeId;
    private LocalDateTime departureTime;

}
