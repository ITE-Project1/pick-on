package com.ite.pickon.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderReq {
    private String productId;
    private int quantity;
    private Long storeId;
    private boolean directPickup;
}
