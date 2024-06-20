package com.ite.pickon.domain.order.service;

import com.ite.pickon.domain.order.OrderStatus;
import com.ite.pickon.domain.order.dto.MultiOrderRes;
import com.ite.pickon.domain.order.dto.OrderRes;
import com.ite.pickon.domain.order.dto.OrderReq;
import com.ite.pickon.domain.order.mapper.OrderMapper;
import com.ite.pickon.domain.sms.service.SmsService;
import com.ite.pickon.domain.stock.service.StockService;
import com.ite.pickon.domain.transport.TransportStatus;
import com.ite.pickon.domain.transport.dto.TransportVO;
import com.ite.pickon.domain.transport.mapper.TransportMapper;
import com.ite.pickon.domain.transport.service.TransportService;
import com.ite.pickon.domain.user.dto.UserVO;
import com.ite.pickon.domain.user.mapper.UserMapper;
import com.ite.pickon.domain.user.service.UserService;
import com.ite.pickon.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.ite.pickon.exception.ErrorCode.FIND_FAIL_ORDER_ID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final TransportMapper transportMapper;
    private final UserMapper userMapper;
    private final TransportService transportService;
    private final StockService stockService;
    private final SmsService smsService;

    // 주문하기 & 재고 요청하기
    @Override
    @Transactional
    public void addOrder(Long userId, OrderReq orderReq) {
        // 주문코드 생성
        orderReq.setOrderId(generateOrderId(orderReq.getStoreId()));

        // 최적의 운송 스케줄 가져오기
        TransportVO transportVO = null;
        int stockUpdateStore = orderReq.getStoreId();
        if (orderReq.getDirectPickup() == 0) {
            transportVO = transportService.findOptimalTransportStore(
                    orderReq.getProductId(),
                    orderReq.getQuantity(),
                    orderReq.getStoreId(),
                    new Date()
            );
        }

        // 주문 생성
        processOrder(userId, orderReq, transportVO);

        // 운송 요청 생성
        if (transportVO != null) {
            addTransportRequest(orderReq, transportVO);
            stockUpdateStore = transportVO.getFromStoreId();
        }

        // 재고 조정
        stockService.updateStock(stockUpdateStore, orderReq.getProductId(), -orderReq.getQuantity());

        // TODO: 주문 정보 가져오기
        OrderRes orderRes = orderMapper.selectOrderById(orderReq.getOrderId());

        // 문자 내용 생성
        String message = String.format("[픽온] 주문이 완료되었습니다.\n" +
                                        "주문 번호: %s\n" +
                                        "픽업 예상 날짜: %s\n" +
                                        "수령 지점: %s\n" +
                                        "상품명: %s\n" +
                                        "지점별 영업 시간 확인 후 방문 부탁드립니다.",
                orderReq.getOrderId(),
                orderReq.getDirectPickup() == 1 ? "즉시 가능" : transportVO.getArrivalTime().toString(),
                orderRes.getToStore(),
                orderRes.getProductName());

        // 문자 전송
        smsService.sendSms(orderRes.getUserPhoneNumber(), message);
    }

    // 주문코드 생성
    private String generateOrderId(int storeId) {
        String datePart = new SimpleDateFormat("yyMMdd").format(new Date());
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 4).toUpperCase();
        return "PO" + storeId + datePart + uuidPart;
    }

    // 주문 생성
    private void processOrder(Long userId, OrderReq orderReq, TransportVO transportVO) {
        LocalDateTime pickupDate;
        int status;
        if (orderReq.getDirectPickup() == 1) {
            // 바로 픽업 가능한 경우
            pickupDate = LocalDateTime.now();
            status = OrderStatus.PICKUPREADY.getStatusCode();
        } else {
            // 지점 간 상품 운송 필요한 경우
            pickupDate = transportVO.getArrivalTime();
            status = OrderStatus.PENDING.getStatusCode();
        }
        orderReq.setStatus(status);
        orderMapper.insertOrder(userId, orderReq, pickupDate);
    }

    // 운송 요청 생성
    private void addTransportRequest(OrderReq orderReq, TransportVO transportVO) {
        orderMapper.insertTransportRequest(orderReq, transportVO.getFromStoreId());
    }

    // 주문 목록 조회
    @Override
    @Transactional
    public List<MultiOrderRes> findOrderList(String storeId, int page, int pageSize, String keyword) {
        int offset = (page - 1) * pageSize;
        return orderMapper.selectOrderListByStore(storeId, offset, pageSize, keyword);
    }

    // 주문 상세조회
    @Override
    @Transactional
    public OrderRes findOrderDetail(String orderId) {
        OrderRes order = orderMapper.selectOrderById(orderId);
        if (order == null) {
            throw new CustomException(FIND_FAIL_ORDER_ID);
        }
        return order;
    }

    // 고객 픽업 완료
    @Override
    @Transactional
    public void modifyOrderStatus(String orderId, OrderStatus status) {
        int updatedRows = orderMapper.updateOrderStatus(orderId, status.getStatusCode());
        if (updatedRows == 0) {
            throw new CustomException(FIND_FAIL_ORDER_ID);
        }
    }

    // 지점 간 배송 완료
    @Override
    @Transactional
    public void modifyOrderAndTransportStatus(List<String> orderIds, OrderStatus orderStatus, TransportStatus transportStatus) {

        orderMapper.batchUpdateOrderStatus(orderIds, orderStatus.getStatusCode());
        transportMapper.batchUpdateTransportRequestStatus(orderIds, transportStatus.getStatusCode());
    }

}
