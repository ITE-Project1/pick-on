package com.ite.pickon.domain.transport;

import lombok.ToString;

import java.time.LocalTime;

@ToString
public enum TransportSchedule {
    STORE_1(1, LocalTime.of(8, 30)),   // 1번 지점, 출발 시간 08:00
    STORE_2(2, LocalTime.of(9, 0)),   // 2번 지점, 출발 시간 09:00
    STORE_3(3, LocalTime.of(10, 0)),  // 3번 지점, 출발 시간 10:00
    STORE_4(4, LocalTime.of(11, 0)),  // 4번 지점, 출발 시간 11:00
    STORE_5(5, LocalTime.of(12, 0));  // 5번 지점, 출발 시간 12:00

    private final int storeId;
    private final LocalTime departureTime;

    TransportSchedule(int storeId, LocalTime departureTime) {
        this.storeId = storeId;
        this.departureTime = departureTime;
    }

    public int getStoreId() {
        return storeId;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }
}
