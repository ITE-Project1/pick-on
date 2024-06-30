package com.ite.pickon.domain.order.service;

import com.ite.pickon.domain.order.OrderStatus;
import com.ite.pickon.domain.order.dto.OrderVO;
import com.ite.pickon.domain.order.dto.OrderInfoVO;
import com.ite.pickon.domain.order.event.OrderCompletedEvent;
import com.ite.pickon.domain.order.mapper.OrderMapper;
import com.ite.pickon.domain.sms.service.SmsService;
import com.ite.pickon.domain.sms.template.SmsMessageTemplate;
import com.ite.pickon.domain.stock.service.StockService;
import com.ite.pickon.domain.transport.TransportStatus;
import com.ite.pickon.domain.transport.dto.TransportVO;
import com.ite.pickon.domain.transport.mapper.TransportMapper;
import com.ite.pickon.domain.transport.service.TransportService;
import com.ite.pickon.exception.CustomException;
import com.ite.pickon.exception.ErrorCode;
import com.ite.pickon.response.ListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final TransportService transportService;
    private final StockService stockService;
    private final SmsService smsService;
    private final SmsMessageTemplate smsMessageTemplate;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 주문 생성 및 재고 요청 처리
     *
     * @param orderVO 주문 요청 객체
     * @return 생성된 주문에 대한 응답 객체
     */
    @Override
    @Transactional
    public OrderInfoVO addOrder(OrderVO orderVO) {
        // 주문코드 생성
        generateOrderCode(orderVO);

        // 최적의 운송 스케줄 가져오기
        TransportVO transportVO = determineTransportVO(orderVO);

        // 재고 조정 지점 지정
        int stockUpdateStore = transportVO != null ? transportVO.getFromStoreId() : orderVO.getStoreId();

        // 바로 픽업 여부에 따른 픽업 예상 날짜, 주문 상태, 상품 운송 출발 지점 결정
        setOrderStatusAndPickupDate(orderVO, transportVO);

       try {
           // (1)주문, (2)재고수량조절, {(3)운송요청생성} Stored Procedure 실행
           orderMapper.insertOrderAndRequest(orderVO);
       } catch (Exception e) {
           throw new CustomException(ErrorCode.FAIL_ORDER);
       }

        // 주문 정보 조회
        OrderInfoVO orderInfoVO = orderMapper.selectOrderById(orderVO.getOrderId());

        // 문자 내용 생성
        String message = smsMessageTemplate.getOrderCompletionMessage(
                orderVO.getOrderId(),
                orderVO.getDirectPickup() == 1 ? "즉시 가능" : transportVO.getArrivalTime().toString(),
                orderInfoVO.getToStore(),
                orderInfoVO.getProductName()
        );

        // 주문 완료 이벤트 발생 (SMS 전송)
        eventPublisher.publishEvent(new OrderCompletedEvent(orderInfoVO.getUserPhoneNumber(), message));

        return orderInfoVO;
    }

    /**
     * 주문코드 생성
     *
     * @param orderVO 주문 요청 객체
     */
    private void generateOrderCode(OrderVO orderVO) {
        String orderId = "PO" + orderVO.getStoreId() + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        orderVO.setOrderId(orderId);
    }

    /**
     * 최적의 운송 스케줄 결정
     *
     * @param orderVO 주문 요청 객체
     * @return 최적의 운송 정보 객체
     */
    private TransportVO determineTransportVO(OrderVO orderVO) {
        if (orderVO.getDirectPickup() == 0) {
            return transportService.findOptimalTransportStore(
                    orderVO.getProductId(),
                    orderVO.getQuantity(),
                    orderVO.getStoreId(),
                    new Date()
            );
        }
        return null;
    }

    /**
     * 픽업 예상 날짜, 주문 상태, 상품 운송 출발 지점 결정
     *
     * @param orderVO 주문 요청 객체
     * @param transportVO 운송 정보 객체
     */
    private void setOrderStatusAndPickupDate(OrderVO orderVO, TransportVO transportVO) {
        LocalDateTime pickupDate;
        int status;
        Integer fromStoreId;
        if (orderVO.getDirectPickup() == 1) {
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
        orderVO.setStatus(status);
        orderVO.setPickupDate(pickupDate);
        orderVO.setFromStoreId(fromStoreId);
    }

    /**
     * 지점별 주문 목록 조회
     *
     * @param storeId 지점 ID
     * @param pageable 페이징 정보
     * @param keyword 검색 키워드
     * @param totalPage 전체 페이지 수
     * @return 주문 목록 응답 객체
     */
    @Override
    @Transactional
    public ListResponse findOrderList(int storeId, Pageable pageable, String keyword, int totalPage) {
        List<OrderInfoVO> orderList =  orderMapper.selectOrderListByStore(storeId, pageable, keyword);
        if(orderList.size() == 0) {
            throw  new CustomException(ErrorCode.FIND_FAIL_ORDERS);
        }
        return new ListResponse(orderList, totalPage);
    }

    /**
     * 지점별 주문 목록 전체 페이지 수 조회
     *
     * @param storeId 지점 ID
     * @param keyword 검색 키워드
     * @param pageSize 페이지 크기
     * @return 전체 페이지 수
     */
    @Override
    public int getTotalPage(int storeId, String keyword, int pageSize) {
        return orderMapper.countTotalOrderPages(storeId, keyword, pageSize);
    }

    /**
     * 나의 주문 내역 조회
     *
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @param totalPage 전체 페이지 수
     * @return 나의 주문 목록 응답 객체
     */
    @Override
    public ListResponse findMyOrderList(Long userId, Pageable pageable, int totalPage) {
        try {
            List<OrderInfoVO> orderList =  orderMapper.selectMyOrderList(userId, pageable);
            if(orderList.size() == 0) {
                throw  new CustomException(ErrorCode.FIND_FAIL_ORDERS);
            }
            return new ListResponse(orderList, totalPage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 나의 주문 목록 전체 페이지 수 조회
     *
     * @param userId 사용자 ID
     * @param pageSize 페이지 크기
     * @return 전체 페이지 수
     */
    @Override
    public int getTotalBasePage(Long userId, int pageSize) {
        return orderMapper.countTotalOrderBasePages(userId, pageSize);
    }

    /**
     * 주문 상세 조회
     *
     * @param orderId 주문 ID
     * @return 주문 응답 객체
     */
    @Override
    @Transactional
    public OrderInfoVO findOrderDetail(String orderId) {
        OrderInfoVO order = orderMapper.selectOrderById(orderId);
        if (order == null) {
            throw new CustomException(FIND_FAIL_ORDER_ID);
        }
        return order;
    }

    /**
     * 주문 상태 변경
     *
     * @param orderId 주문 ID
     * @param status 주문 상태
     */
    @Override
    @Transactional
    public void modifyOrderStatus(String orderId, OrderStatus status) {
        int updatedRows = orderMapper.updateOrderStatus(orderId, status.getStatusCode());
        if (updatedRows == 0) {
            throw new CustomException(FIND_FAIL_ORDER_ID);
        }
    }

    /**
     * 지점 간 배송 완료 및 주문 상태 변경
     *
     * @param orderIds 주문 ID 목록
     * @param orderStatus 주문 상태
     * @param transportStatus 운송 상태
     */
    @Override
    @Transactional
    public void modifyOrderAndTransportStatus(List<String> orderIds, OrderStatus orderStatus, TransportStatus transportStatus) {

        try {
            orderMapper.batchUpdateOrderStatus(orderIds, orderStatus.getStatusCode());
            transportMapper.batchUpdateTransportRequestStatus(orderIds, transportStatus.getStatusCode());

            // 주문 정보 조회
            List<OrderInfoVO> orderList = orderMapper.selectOrderListById(orderIds);

            // 각 사용자에게 SMS 전송
            for (OrderInfoVO order : orderList) {
                String message = smsMessageTemplate.getPickUpReadyMessage(
                        order.getOrderId(),
                        order.getToStore(),
                        order.getProductName()
                );
                // 픽업 가능 이벤트 발생 (SMS 전송)
                eventPublisher.publishEvent(new OrderCompletedEvent(order.getUserPhoneNumber(), message));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
