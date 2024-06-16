package com.ite.pickon.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderVO {
    private String orderId;
    private Long userId;
    private Long storeId;
    private String productId;
    private LocalDateTime pickupDate;
    private int quantity;
    private double totalPrice;
    private int status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
