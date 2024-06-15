package com.ite.pickon.domain.transport.service;

import com.ite.pickon.domain.transport.dto.TransportVO;

import java.util.Date;

public interface TransportService {
    TransportVO findOptimalTransportStore(String productId,
                                          int quantity,
                                          int storeId,
                                          Date orderDate);

    void modifyTransportStatus(String orderId);
}
