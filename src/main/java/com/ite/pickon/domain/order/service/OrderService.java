package com.ite.pickon.domain.order.service;

import com.ite.pickon.domain.order.OrderStatus;
import com.ite.pickon.domain.order.dto.OrderRequest;
import com.ite.pickon.domain.order.dto.OrderResponse;
import com.ite.pickon.domain.transport.TransportStatus;
import com.ite.pickon.response.ListResponse;

import java.util.List;

public interface OrderService {

    void addOrder(Long userId, OrderRequest orderRequest);

    OrderResponse findOrderDetail(String orderId);

    void modifyOrderStatus(String orderId, OrderStatus completed);

    ListResponse findOrderList(int storeId, int page, int PAGE_SIZE, String keyword, int totalPage);

    void modifyOrderAndTransportStatus(List<String> orderIds, OrderStatus pickupready, TransportStatus completed);

    int getTotalPage(int storeId, String keyword, int pageSize);
}
