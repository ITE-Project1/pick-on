package com.ite.pickon.domain.order.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderVO {
    private Long userId;
    private String orderId;
    private String productId;
    private int quantity;
    private int storeId;            // 픽업 지점
    private Integer fromStoreId;        // 지점 간 상품 운송이 있을 시, 상품 배송 시작 지점
    private int directPickup;
    private int status;
    private LocalDateTime pickupDate;
}
