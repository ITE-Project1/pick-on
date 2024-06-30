package com.ite.pickon.domain.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class TransportVO {
    private int fromStoreId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

}
