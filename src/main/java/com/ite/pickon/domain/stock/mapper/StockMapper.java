package com.ite.pickon.domain.stock.mapper;

import com.ite.pickon.domain.stock.dto.StockVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StockMapper {

    // 지점별 상품의 재고 조회
    List<StockVO> selectStockForStore(String productId);

    // 재고 수량 업데이트
    void updateStockQuantity(@Param("storeId") int storeId,
                             @Param("productId") String productId,
                             @Param("quantityChange") int quantityChange);
}
