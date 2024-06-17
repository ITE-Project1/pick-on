package com.ite.pickon.domain.stock.service;

import com.ite.pickon.domain.stock.mapper.StockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockMapper stockMapper;

    @Transactional
    public void updateStock(int storeId, String productId, int quantityChange) {
        stockMapper.updateStockQuantity(storeId, productId, quantityChange);
    }
}
