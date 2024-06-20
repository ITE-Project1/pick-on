package com.ite.pickon.domain.order.controller;

import com.ite.pickon.domain.order.OrderStatus;
import com.ite.pickon.domain.order.dto.MultiOrderResponse;
import com.ite.pickon.domain.order.dto.OrderRequest;
import com.ite.pickon.domain.order.dto.OrderResponse;
import com.ite.pickon.domain.order.service.OrderService;
import com.ite.pickon.domain.transport.TransportStatus;
import com.ite.pickon.domain.user.service.UserService;
import com.ite.pickon.response.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    private static final int PAGE_SIZE = 10;

    /**
     * 주문하기
     * [POST] /orders
     */
    @PostMapping(value="/orders", produces = "application/json; charset=UTF-8")
    public ResponseEntity<SimpleResponse> orderAdd(HttpSession session, @RequestBody OrderRequest orderRequest) {
        System.out.println("Received order request: " + orderRequest);

        Long userId = userService.checkCurrentUser(session);
        orderService.addOrder(userId, orderRequest);
        return new ResponseEntity<>(new SimpleResponse("주문이 완료되었습니다."), HttpStatus.OK);
    }

    /**
     * 지점별 주문 내역 조회
     * [GET] /admin/orders?storeId={지점인덱스}&page={페이지번호}&keyword={검색키워드}
     */
    @GetMapping("/admin/orders")
    public ResponseEntity<List<MultiOrderResponse>> orderList(@RequestParam String storeId,
                                                              @RequestParam int page,
                                                              @RequestParam(required = false) String keyword) {

        return new ResponseEntity<>(orderService.findOrderList(storeId, page, PAGE_SIZE, keyword), HttpStatus.OK);
    }


    /**
     *  주문 상세조회
     *  [GET] /admin/orders/:{orderId}
     */
    @GetMapping("/admin/orders/{orderId}")
    public ResponseEntity<OrderResponse> orderDetails(@PathVariable String orderId) {
        OrderResponse orderResponse = orderService.findOrderDetail(orderId);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    /**
     *  고객 픽업 완료
     *  [PATCH] /admin/orders/:{orderId}/status/completed
     */
    @PatchMapping(value = "/admin/orders/{orderId}/status/completed", produces = "application/json; charset=UTF-8")
    public ResponseEntity<SimpleResponse> orderCompletedModify(@PathVariable String orderId) {
        orderService.modifyOrderStatus(orderId, OrderStatus.COMPLETED);
        return new ResponseEntity<>(new SimpleResponse("고객 픽업이 완료되었습니다."), HttpStatus.OK);
    }

    /**
     *  지점간 상품 운송 완료
     *  [PATCH] /admin/orders/status/pickupready
     */
    @PatchMapping("/admin/orders/status/pickupready")
    public ResponseEntity<SimpleResponse> updateOrderStatusToPickupReady(@RequestBody List<String> orderIds) {
        orderService.modifyOrderAndTransportStatus(orderIds, OrderStatus.PICKUPREADY, TransportStatus.COMPLETED);
        return new ResponseEntity<>(new SimpleResponse("지점간 상품 배송이 완료되었습니다."), HttpStatus.OK);
    }

}
