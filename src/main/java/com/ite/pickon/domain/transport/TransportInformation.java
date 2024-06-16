package com.ite.pickon.domain.transport;

public enum TransportInformation {
    TRANSPORT_1(1, 2, 60),
    TRANSPORT_2(1, 3, 30),
    TRANSPORT_3(1, 4, 50),
    TRANSPORT_4(1, 5, 20),
    TRANSPORT_5(2, 1, 40),
    TRANSPORT_6(2, 3, 30),
    TRANSPORT_7(2, 4, 20),
    TRANSPORT_8(2, 5, 25),
    TRANSPORT_9(3, 1, 40),
    TRANSPORT_10(3, 2, 50),
    TRANSPORT_11(3, 4, 25),
    TRANSPORT_12(3, 5, 35),
    TRANSPORT_13(4, 1, 40),
    TRANSPORT_14(4, 2, 30),
    TRANSPORT_15(4, 3, 50),
    TRANSPORT_16(4, 5, 35),
    TRANSPORT_17(5, 1, 25),
    TRANSPORT_18(5, 2, 30),
    TRANSPORT_19(5, 3, 40),
    TRANSPORT_20(5, 4, 50);

    private final int toStoreId;
    private final int fromStoreId;
    private final int transportMinutes;

    TransportInformation(int toStoreId, int fromStoreId, int transportMinutes) {
        this.toStoreId = toStoreId;
        this.fromStoreId = fromStoreId;
        this.transportMinutes = transportMinutes;
    }

    public int getToStoreId() {
        return toStoreId;
    }

    public int getFromStoreId() {
        return fromStoreId;
    }

    public int getTransportMinutes() {
        return transportMinutes;
    }
}
