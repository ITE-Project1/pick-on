package com.ite.pickon.domain.order.service;

import com.ite.pickon.domain.order.OrderStatus;
import com.ite.pickon.domain.order.dto.MultiOrderRes;
import com.ite.pickon.domain.order.dto.OrderRes;
import com.ite.pickon.domain.order.dto.OrderReq;
import com.ite.pickon.domain.transport.TransportStatus;

import java.util.List;

public interface OrderService {

    void addOrder(Long userId, OrderReq orderReq);

    OrderRes findOrderDetail(String orderId);

    void modifyOrderStatus(String orderId, OrderStatus completed);

    List<MultiOrderRes> findOrderList(String storeId, int page, int PAGE_SIZE, String keyword);

    void modifyOrderAndTransportStatus(List<String> orderIds, OrderStatus pickupready, TransportStatus completed);
}
