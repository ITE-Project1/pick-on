package com.ite.pickon.domain.order.service;

import com.ite.pickon.domain.order.dto.GetOrderRes;
import com.ite.pickon.domain.order.dto.PostOrderReq;

public interface OrderService {

    void createOrder(Long userId, PostOrderReq orderReq);

    GetOrderRes getOrderById(String orderId);
}
