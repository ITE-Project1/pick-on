package com.ite.pickon.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MultiOrderResponse {
    private String orderId;
    private int quantity;
    private String fromStore;
    private String pickupStatus;
}
