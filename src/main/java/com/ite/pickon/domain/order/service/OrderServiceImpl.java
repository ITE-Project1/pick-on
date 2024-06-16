package com.ite.pickon.domain.order.service;

import com.ite.pickon.domain.order.OrderStatus;
import com.ite.pickon.domain.order.dto.MultiOrderRes;
import com.ite.pickon.domain.order.dto.OrderRes;
import com.ite.pickon.domain.order.dto.OrderReq;
import com.ite.pickon.domain.order.mapper.OrderMapper;
import com.ite.pickon.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.ite.pickon.exception.ErrorCode.FIND_FAIL_ORDER_ID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    // 주문하기
    @Override
    public void addOrder(Long userId, OrderReq orderReq) {
        orderReq.setOrderId(generateOrderId(orderReq.getStoreId()));
        if (orderReq.getDirectPickup() == 1) {
            orderMapper.insertOrder(userId, orderReq);
        } else {
//            orderMapper.insertOrder(userId, orderReq);
//            orderMapper.insertTransportRequest(userId, orderReq);
        }
    }

    // 주문 목록 조회
    @Override
    public List<MultiOrderRes> findOrderList(String storeId, int page, int pageSize, String keyword) {
        int offset = (page - 1) * pageSize;
        return orderMapper.selectOrderListByStore(storeId, offset, pageSize, keyword);
    }

    // 주문 상세조회
    @Override
    public OrderRes findOrderDetail(String orderId) {
        OrderRes order = orderMapper.selectOrderById(orderId);
        if (order == null) {
            throw new CustomException(FIND_FAIL_ORDER_ID);
        }
        return order;
    }

    // 고객 픽업 완료
    @Override
    public void modifyOrderStatus(String orderId, OrderStatus status) {
        int updatedRows = orderMapper.updateOrderStatus(orderId, status.getStatusCode());
        if (updatedRows == 0) {
            throw new CustomException(FIND_FAIL_ORDER_ID);
        }
    }

    // 주문코드 생성
//    private String generateOrderId() {
//        return "PO" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
//    }

    private String generateOrderId(Long storeId) {
        String datePart = new SimpleDateFormat("yyMMdd").format(new Date());
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 4).toUpperCase();
        return "PO" + storeId + datePart + uuidPart;
    }


}
