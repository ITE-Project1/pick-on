package com.ite.pickon.domain.order.controller;

import com.ite.pickon.domain.order.dto.GetOrderRes;
import com.ite.pickon.domain.order.dto.PostOrderReq;
import com.ite.pickon.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

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
     *  주문 상세조회
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<GetOrderRes> getOrderById(@PathVariable String orderId) {
        GetOrderRes getOrderRes = orderService.getOrderById(orderId);
        return new ResponseEntity<>(getOrderRes, HttpStatus.OK);
    }

}
