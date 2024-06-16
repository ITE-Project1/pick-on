package com.ite.pickon.domain.order.controller;

import com.ite.pickon.domain.order.OrderStatus;
import com.ite.pickon.domain.order.dto.MultiOrderRes;
import com.ite.pickon.domain.order.dto.OrderRes;
import com.ite.pickon.domain.order.service.OrderService;
import com.ite.pickon.response.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private static final int PAGE_SIZE = 10;

//    /**
//     * 주문하기
//     * [POST] /orders
//     */
//    @PostMapping("")
//    @ResponseBody
//    public ResponseEntity<String> createOrder(@RequestParam("userId") Long userId,
//                                              @RequestBody PostOrderReq orderReq) {
//        orderService.createOrder(userId, orderReq);
//        return new ResponseEntity<>("주문이 완료되었습니다.", HttpStatus.OK);
//    }

    /**
     * 지점별 주문 내역 조회
     */
    @GetMapping("")
    public ResponseEntity<List<MultiOrderRes>> getOrdersByStoreId(@RequestParam String storeId,
                                                                  @RequestParam int page,
                                                                  @RequestParam(required = false) String keyword) {

        return new ResponseEntity<>(orderService.getOrdersByStoreId(storeId, page, PAGE_SIZE, keyword), HttpStatus.OK);
    }


    /**
     *  주문 상세조회
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderRes> getOrderById(@PathVariable String orderId) {
        OrderRes orderRes = orderService.getOrderById(orderId);
        return new ResponseEntity<>(orderRes, HttpStatus.OK);
    }

    /**
     *  고객 픽업 완료
     */
    @PatchMapping(value = "/{orderId}/status/completed", produces = "application/json; charset=UTF-8")
    public ResponseEntity<SimpleResponse> updateOrderAsCompleted(@PathVariable String orderId) {
        orderService.updateOrderStatus(orderId, OrderStatus.COMPLETED);
        return new ResponseEntity<>(new SimpleResponse("고객 픽업이 완료되었습니다."), HttpStatus.OK);
    }

//    @PatchMapping("/{orderId}/status/pickupready")
//    public ResponseEntity<String> updateOrderStatusToPickupReady(@PathVariable String orderId) {
//        orderService.updateOrderStatus(orderId, OrderStatus.PICKUPREADY);
//        return new ResponseEntity<>("주문 상태가 PICKUPREADY로 업데이트되었습니다.", HttpStatus.OK);
//    }

}
