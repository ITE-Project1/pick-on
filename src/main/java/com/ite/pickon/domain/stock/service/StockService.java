package com.ite.pickon.domain.stock.service;

import com.ite.pickon.domain.stock.dto.StockVO;

public interface StockService {

    // 재고 생성
    void addStock(StockVO stockVO);
}
