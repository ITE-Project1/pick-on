package com.ite.pickon.domain.order.service;

import com.ite.pickon.domain.order.dto.GetOrderRes;
import com.ite.pickon.domain.order.dto.PostOrderReq;
import com.ite.pickon.domain.order.mapper.OrderMapper;
import com.ite.pickon.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ite.pickon.exception.ErrorCode.FIND_FAIL_ORDER_ID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    // 주문하기
    @Override
    public void createOrder(Long userId, PostOrderReq orderReq) {

    }

    // 주문 상세조회
    @Override
    public GetOrderRes getOrderById(String orderId) {
        GetOrderRes order = orderMapper.selectOrderById(orderId);
        if (order == null) {
            throw new CustomException(FIND_FAIL_ORDER_ID);
        }
        return order;
    }
}
