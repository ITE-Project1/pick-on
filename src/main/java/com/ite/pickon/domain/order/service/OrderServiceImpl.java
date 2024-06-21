package com.ite.pickon.domain.order.service;

import com.ite.pickon.domain.order.OrderStatus;
import com.ite.pickon.domain.order.dto.MultiOrderResponse;
import com.ite.pickon.domain.order.dto.MyOrderResponse;
import com.ite.pickon.domain.order.dto.OrderRequest;
import com.ite.pickon.domain.order.dto.OrderResponse;
import com.ite.pickon.domain.order.mapper.OrderMapper;
import com.ite.pickon.domain.sms.service.SmsService;
import com.ite.pickon.domain.sms.template.SmsMessageTemplate;
import com.ite.pickon.domain.stock.service.StockService;
import com.ite.pickon.domain.transport.TransportStatus;
import com.ite.pickon.domain.transport.dto.TransportVO;
import com.ite.pickon.domain.transport.mapper.TransportMapper;
import com.ite.pickon.domain.transport.service.TransportService;
import com.ite.pickon.domain.user.dto.UserVO;
import com.ite.pickon.domain.user.mapper.UserMapper;
import com.ite.pickon.exception.CustomException;
import com.ite.pickon.exception.ErrorCode;
import com.ite.pickon.response.ListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final SmsMessageTemplate smsMessageTemplate;

    // 주문하기 & 재고 요청하기
    @Override
    @Transactional
    public void addOrder(OrderRequest orderRequest) {
        long startTime = System.currentTimeMillis();

        // 주문코드 생성
        generateOrderCode(orderRequest);

        // 최적의 운송 스케줄 가져오기
        TransportVO transportVO = determineTransportVO(orderRequest);

        // 재고 조정 지점 지정
        int stockUpdateStore = transportVO != null ? transportVO.getFromStoreId() : orderRequest.getStoreId();

        // 바로 픽업 여부에 따른 픽업 예상 날짜, 주문 상태, 상품 운송 출발 지점 결졍
        setOrderStatusAndPickupDate(orderRequest, transportVO);

        // 주문 및 운송 요청 생성
        orderMapper.insertOrderAndRequest(orderRequest);

        // 재고 조정
        stockService.updateStock(stockUpdateStore, orderRequest.getProductId(), -orderRequest.getQuantity());

        // 주문 정보 조회
        OrderResponse orderResponse = orderMapper.selectOrderById(orderRequest.getOrderId());

        // 문자 내용 생성
        String message = smsMessageTemplate.getOrderCompletionMessage(
                orderRequest.getOrderId(),
                orderRequest.getDirectPickup() == 1 ? "즉시 가능" : transportVO.getArrivalTime().toString(),
                orderResponse.getToStore(),
                orderResponse.getProductName()
        );

        // 문자 전송
        smsService.sendSms(orderResponse.getUserPhoneNumber(), message);

        long endTime = System.currentTimeMillis();
        System.out.println("프로시저 방식 실행 시간: " + (endTime - startTime) + "ms");
    }

    // 주문코드 생성
    private void generateOrderCode(OrderRequest orderRequest) {
        String orderId = "PO" + orderRequest.getStoreId() + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        orderRequest.setOrderId(orderId);
    }

    // 최적의 운송 스케줄
    private TransportVO determineTransportVO(OrderRequest orderRequest) {
        if (orderRequest.getDirectPickup() == 0) {
            return transportService.findOptimalTransportStore(
                    orderRequest.getProductId(),
                    orderRequest.getQuantity(),
                    orderRequest.getStoreId(),
                    new Date()
            );
        }
        return null;
    }

    // 픽업 예상 날짜, 주문 상태, 상품 운송 출발 지점 결졍
    private void setOrderStatusAndPickupDate(OrderRequest orderRequest, TransportVO transportVO) {
        LocalDateTime pickupDate;
        int status;
        Integer fromStoreId;
        if (orderRequest.getDirectPickup() == 1) {
            // 바로 픽업
            pickupDate = LocalDateTime.now();
            status = OrderStatus.PICKUPREADY.getStatusCode();
            fromStoreId = null;
        } else {
            // 지점 간 상품 운송
            pickupDate = transportVO.getArrivalTime();
            status = OrderStatus.PENDING.getStatusCode();
            fromStoreId = transportVO.getFromStoreId();
        }
        orderRequest.setStatus(status);
        orderRequest.setPickupDate(pickupDate);
        orderRequest.setFromStoreId(fromStoreId);
    }

    // 주문 목록 조회
    @Override
    @Transactional
    public ListResponse findOrderList(int storeId, Pageable pageable, String keyword, int totalPage) {
        List<MultiOrderResponse> orderList =  orderMapper.selectOrderListByStore(storeId, pageable, keyword);
        if(orderList.size() == 0) {
            throw  new CustomException(ErrorCode.FIND_FAIL_ORDERS);
        }
        return new ListResponse(orderList, totalPage);
    }

    // 지점별 주문 목록 전체 페이지 개수 조회
    @Override
    public int getTotalPage(int storeId, String keyword, int pageSize) {
        return orderMapper.countTotalOrderPages(storeId, keyword, pageSize);
    }

    // 나의 주문 내역 조회
    @Override
    public ListResponse findMyOrderList(Long userId, Pageable pageable, int totalPage) {
        try {
            List<MyOrderResponse> orderList =  orderMapper.selectMyOrderList(userId, pageable);
            if(orderList.size() == 0) {
                throw  new CustomException(ErrorCode.FIND_FAIL_ORDERS);
            }
            return new ListResponse(orderList, totalPage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 나의 주문 목록 전체 페이지 개수 조회
    @Override
    public int getTotalBasePage(Long userId, int pageSize) {
        return orderMapper.countTotalOrderBasePages(userId, pageSize);
    }

    // 주문 상세조회
    @Override
    @Transactional
    public OrderResponse findOrderDetail(String orderId) {
        OrderResponse order = orderMapper.selectOrderById(orderId);
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

        try {
            orderMapper.batchUpdateOrderStatus(orderIds, orderStatus.getStatusCode());
            transportMapper.batchUpdateTransportRequestStatus(orderIds, transportStatus.getStatusCode());

            // 주문 정보 조회
            List<OrderResponse> orderList = orderMapper.selectOrderListById(orderIds);

            // 각 사용자에게 SMS 전송
            for (OrderResponse order : orderList) {
                String message = smsMessageTemplate.getPickUpReadyMessage(
                        order.getOrderId(),
                        order.getToStore(),
                        order.getProductName()
                );
                smsService.sendSms(order.getUserPhoneNumber(), message);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
