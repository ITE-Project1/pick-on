package com.ite.pickon.domain.transport;

public enum TransportInformation {
    TRANSPORT_1(1, 2, 60),
    TRANSPORT_2(1, 3, 90),
    TRANSPORT_3(1, 4, 120),
    TRANSPORT_4(1, 5, 150),
    TRANSPORT_5(2, 1, 60),
    TRANSPORT_6(2, 3, 45),
    TRANSPORT_7(2, 4, 75),
    TRANSPORT_8(2, 5, 105),
    TRANSPORT_9(3, 1, 90),
    TRANSPORT_10(3, 2, 45),
    TRANSPORT_11(3, 4, 30),
    TRANSPORT_12(3, 5, 60),
    TRANSPORT_13(4, 1, 120),
    TRANSPORT_14(4, 2, 75),
    TRANSPORT_15(4, 3, 30),
    TRANSPORT_16(4, 5, 45),
    TRANSPORT_17(5, 1, 150),
    TRANSPORT_18(5, 2, 105),
    TRANSPORT_19(5, 3, 60),
    TRANSPORT_20(5, 4, 45);

    private final int toStoreId;
    private final int fromStoreId;
    private final int transportTime;

    TransportInformation(int toStoreId, int fromStoreId, int transportTime) {
        this.toStoreId = toStoreId;
        this.fromStoreId = fromStoreId;
        this.transportTime = transportTime;
    }

    public int getToStoreId() {
        return toStoreId;
    }

    public int getFromStoreId() {
        return fromStoreId;
    }

    public int getTransportTime() {
        return transportTime;
    }
}
