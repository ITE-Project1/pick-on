package com.ite.pickon.domain.stock.service;

import com.ite.pickon.domain.stock.dto.StockRequest;

public interface StockService {

    // 재고 생성
    void addStock(StockRequest stockRequest);
}
