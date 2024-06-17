package com.ite.pickon.domain.stock.service;

import com.ite.pickon.domain.stock.dto.StockReq;

public interface StockService {

    // 재고 수량 변경
    void updateStock(int fromStoreId, String productId, int i);

    // 재고 생성
    void addStock(StockReq stockReq);
}
