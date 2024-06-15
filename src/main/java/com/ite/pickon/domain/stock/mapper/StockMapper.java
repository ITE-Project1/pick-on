package com.ite.pickon.domain.stock.mapper;

import com.ite.pickon.domain.stock.dto.StockVO;

import java.util.List;

public interface StockMapper {

    // 지점별 상품의 재고 조회
    List<StockVO> selectStockForStore(int productId);


}
