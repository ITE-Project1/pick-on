package com.ite.pickon.domain.stock.dto;

import lombok.Getter;

@Getter
public class StockRequest {
    private int storeId;
    private String productId;
    private int quantity;
}
