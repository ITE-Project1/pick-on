package com.ite.pickon.domain.order.service;

import com.ite.pickon.domain.order.OrderStatus;
import com.ite.pickon.domain.order.dto.MultiOrderResponse;
import com.ite.pickon.domain.order.dto.OrderRequest;
import com.ite.pickon.domain.order.dto.OrderResponse;
import com.ite.pickon.domain.transport.TransportStatus;

import java.util.List;

public interface OrderService {

    void addOrder(Long userId, OrderRequest orderRequest);

    OrderResponse findOrderDetail(String orderId);

    void modifyOrderStatus(String orderId, OrderStatus completed);

    List<MultiOrderResponse> findOrderList(String storeId, int page, int PAGE_SIZE, String keyword);

    void modifyOrderAndTransportStatus(List<String> orderIds, OrderStatus pickupready, TransportStatus completed);
}
