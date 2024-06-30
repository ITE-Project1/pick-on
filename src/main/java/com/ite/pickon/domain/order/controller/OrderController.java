package com.ite.pickon.domain.order.controller;

import com.ite.pickon.domain.order.OrderStatus;
import com.ite.pickon.domain.order.dto.OrderVO;
import com.ite.pickon.domain.order.dto.OrderInfoVO;
import com.ite.pickon.domain.order.service.OrderService;
import com.ite.pickon.domain.transport.TransportStatus;
import com.ite.pickon.exception.CustomException;
import com.ite.pickon.exception.ErrorCode;
import com.ite.pickon.response.ListResponse;
import com.ite.pickon.response.SimpleResponse;
import com.ite.pickon.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;

    private static final int PAGE_SIZE = 10;

    /**
     * 주문하기
     *
     * @param token 현재 세션
     * @param orderVO 주문 요청 객체
     * @return 주문 응답 객체를 담은 ResponseEntity
     */
    @PostMapping(value = "/orders", produces = "application/json; charset=UTF-8")
    public ResponseEntity<OrderInfoVO> orderAdd(@RequestHeader("Authorization") String token, @RequestBody OrderVO orderVO) {
        orderVO.setUserId(jwtTokenProvider.getUserIdFromToken(token));
        return ResponseEntity.ok(orderService.addOrder(orderVO));
    }

    /**
     * 지점별 주문 내역 조회
     *
     * @param storeId 지점 인덱스
     * @param page 페이지 번호
     * @param keyword 검색 키워드 (선택 사항)
     * @return 주문 목록을 담은 ResponseEntity
     */
    @GetMapping("/admin/orders")
    public ResponseEntity<ListResponse> orderList(@RequestParam int storeId,
                                                  @RequestParam int page,
                                                  @RequestParam(required = false) String keyword) {

        // 전체 페이지 개수
        int totalPage = orderService.getTotalPage(storeId, keyword, PAGE_SIZE);

        // 전체 페이지 개수를 넘는 요청을 보내면 예외 처리
        if (page >= totalPage) {
            throw new CustomException(ErrorCode.FIND_FAIL_PRODUCTS);
        }

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return new ResponseEntity<>(orderService.findOrderList(storeId, pageable, keyword, totalPage), HttpStatus.OK);
    }

    /**
     * 마이 주문 내역
     *
     * @param token 현재 세션
     * @param page 페이지 번호
     * @return 나의 주문 목록을 담은 ResponseEntity
     */
    @GetMapping("/orders")
    public ResponseEntity<ListResponse> myOrderList(@RequestHeader("Authorization") String token, @RequestParam int page) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        // 전체 페이지 개수
        int totalPage = orderService.getTotalBasePage(userId, PAGE_SIZE);

        // 전체 페이지 개수를 넘는 요청을 보내면 예외 처리
        if (page >= totalPage) {
            throw new CustomException(ErrorCode.FIND_FAIL_PRODUCTS);
        }

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return new ResponseEntity<>(orderService.findMyOrderList(userId, pageable, totalPage), HttpStatus.OK);
    }

    /**
     * 주문 상세 조회
     *
     * @param orderId 주문 ID
     * @return 주문 상세 정보를 담은 ResponseEntity
     */
    @GetMapping("/admin/orders/{orderId}")
    public ResponseEntity<OrderInfoVO> orderDetails(@PathVariable String orderId) {
        OrderInfoVO orderInfoVO = orderService.findOrderDetail(orderId);
        return new ResponseEntity<>(orderInfoVO, HttpStatus.OK);
    }

    /**
     * 고객 픽업 완료로 상태 변경
     *
     * @param orderId 주문 ID
     * @return 단순 응답 메시지를 담은 ResponseEntity
     */
    @PatchMapping(value = "/admin/orders/{orderId}/status/completed", produces = "application/json; charset=UTF-8")
    public ResponseEntity<SimpleResponse> orderCompletedModify(@PathVariable String orderId) {
        orderService.modifyOrderStatus(orderId, OrderStatus.COMPLETED);
        return new ResponseEntity<>(new SimpleResponse("고객 픽업이 완료되었습니다."), HttpStatus.OK);
    }

    /**
     * 픽업 준비 완료로 상태 변경
     *
     * @param orderIds 주문 ID 목록
     * @return 단순 응답 메시지를 담은 ResponseEntity
     */
    @PatchMapping("/admin/orders/status/pickupready")
    public ResponseEntity<SimpleResponse> updateOrderStatusToPickupReady(@RequestBody List<String> orderIds) {
        orderService.modifyOrderAndTransportStatus(orderIds, OrderStatus.PICKUPREADY, TransportStatus.COMPLETED);
        return new ResponseEntity<>(new SimpleResponse("지점간 상품 배송이 완료되었습니다."), HttpStatus.OK);
    }
}
