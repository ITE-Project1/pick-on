package com.ite.pickon.domain.order.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderRequest {
    private String orderId;
    private String productId;
    private int quantity;
    private int storeId;
    private int directPickup;
    private int status;
}
