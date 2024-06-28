package com.ite.pickon.domain.transport;

import lombok.ToString;

import java.time.LocalTime;

@ToString
public enum TransportSchedule {
    STORE1_1(1, LocalTime.of(8, 30)),
    STORE1_2(1, LocalTime.of(12, 30)),
    STORE1_3(1, LocalTime.of(15, 30)),
    STORE1_4(1, LocalTime.of(17, 30)),
    STORE2_1(2, LocalTime.of(8, 0)),
    STORE2_2(2, LocalTime.of(11, 0)),
    STORE2_3(2, LocalTime.of(14, 0)),
    STORE2_4(2, LocalTime.of(16, 0)),
    STORE3_1(3, LocalTime.of(9, 0)),
    STORE3_2(3, LocalTime.of(12, 0)),
    STORE3_3(3, LocalTime.of(15, 0)),
    STORE3_4(3, LocalTime.of(18, 0)),
    STORE4_1(4, LocalTime.of(9, 0)),
    STORE4_2(4, LocalTime.of(11, 0)),
    STORE4_3(4, LocalTime.of(13, 0)),
    STORE4_4(4, LocalTime.of(16, 0)),
    STORE5_1(5, LocalTime.of(8, 40)),
    STORE5_2(5, LocalTime.of(11, 40)),
    STORE5_3(5, LocalTime.of(14, 0)),
    STORE5_4(5, LocalTime.of(17, 0));

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
