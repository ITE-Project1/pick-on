package com.ite.pickon.domain.order.service;

import com.ite.pickon.domain.order.OrderStatus;
import com.ite.pickon.domain.order.dto.OrderVO;
import com.ite.pickon.domain.order.dto.OrderInfoVO;
import com.ite.pickon.domain.transport.TransportStatus;
import com.ite.pickon.response.ListResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    OrderInfoVO addOrder(OrderVO orderVO);

    OrderInfoVO findOrderDetail(String orderId);

    void modifyOrderStatus(String orderId, OrderStatus completed);

    ListResponse findOrderList(int storeId, Pageable pageable, String keyword, int totalPage);

    void modifyOrderAndTransportStatus(List<String> orderIds, OrderStatus pickupready, TransportStatus completed);

    int getTotalPage(int storeId, String keyword, int pageSize);

    int getTotalBasePage(Long userId, int pageSize);

    ListResponse findMyOrderList(Long userId, Pageable pageable, int totalPage);
}
