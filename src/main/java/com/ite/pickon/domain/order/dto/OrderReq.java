package com.ite.pickon.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderReq {
    private String orderId;
    private String productId;
    private int quantity;
    private Long storeId;
    private int directPickup;
}
