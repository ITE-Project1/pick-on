package com.ite.pickon.domain.stock.mapper;

import com.ite.pickon.domain.stock.dto.StockRequest;
import com.ite.pickon.domain.stock.dto.StockVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StockMapper {

    /**
     * 지점별 상품의 재고 조회
     *
     * @param productId 조회할 상품의 ID
     * @return 해당 상품의 재고 정보 리스트
     */
    List<StockVO> selectStockForStore(String productId);

    /**
     * 재고 수량 업데이트
     *
     * @param storeId       재고를 업데이트할 지점의 ID
     * @param productId     재고를 업데이트할 상품의 ID
     * @param quantityChange 변경할 재고 수량 (양수: 증가, 음수: 감소)
     */
    void updateStockQuantity(@Param("storeId") int storeId,
                             @Param("productId") String productId,
                             @Param("quantityChange") int quantityChange);

    /**
     * 재고 생성
     *
     * @param stockRequest 생성할 재고의 요청 데이터
     */
    void insertStock(@Param("stockRequest") StockRequest stockRequest);

    /**
     * 해당 지점에 상품 존재 여부 확인
     *
     * @param storeId   확인할 지점의 ID
     * @param productId 확인할 상품의 ID
     * @return 상품 존재 여부 (존재하면 1, 존재하지 않으면 0)
     */
    int checkStockExists(@Param("storeId") int storeId, @Param("productId") String productId);
}
