package com.ite.pickon.domain.order.service;

import com.ite.pickon.domain.order.OrderStatus;
import com.ite.pickon.domain.order.dto.MultiOrderRes;
import com.ite.pickon.domain.order.dto.OrderRes;
import com.ite.pickon.domain.order.dto.OrderReq;

import java.util.List;

public interface OrderService {

    void createOrder(Long userId, OrderReq orderReq);

    OrderRes getOrderById(String orderId);

    void updateOrderStatus(String orderId, OrderStatus completed);

    List<MultiOrderRes> getOrdersByStoreId(String storeId, int page, int PAGE_SIZE, String keyword);
}
