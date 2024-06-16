package com.ite.pickon.domain.order.service;

import com.ite.pickon.domain.order.OrderStatus;
import com.ite.pickon.domain.order.dto.MultiOrderRes;
import com.ite.pickon.domain.order.dto.OrderRes;
import com.ite.pickon.domain.order.dto.OrderReq;
import com.ite.pickon.domain.order.mapper.OrderMapper;
import com.ite.pickon.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ite.pickon.exception.ErrorCode.FIND_FAIL_ORDER_ID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    // 주문하기
    @Override
    public void createOrder(Long userId, OrderReq orderReq) {

    }

    // 주문 목록 조회
    @Override
    public List<MultiOrderRes> getOrdersByStoreId(String storeId, int page, int pageSize, String keyword) {
        int offset = (page - 1) * pageSize;
        return orderMapper.selectOrderListByStore(storeId, offset, pageSize, keyword);
    }

    // 주문 상세조회
    @Override
    public OrderRes getOrderById(String orderId) {
        OrderRes order = orderMapper.selectOrderById(orderId);
        if (order == null) {
            throw new CustomException(FIND_FAIL_ORDER_ID);
        }
        return order;
    }

    // 고객 픽업 완료
    @Override
    public void updateOrderStatus(String orderId, OrderStatus status) {
        int updatedRows = orderMapper.updateOrderStatus(orderId, status.getStatusCode());
        if (updatedRows == 0) {
            throw new CustomException(FIND_FAIL_ORDER_ID);
        }
    }


}
