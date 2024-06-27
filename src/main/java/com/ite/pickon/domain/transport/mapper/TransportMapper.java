package com.ite.pickon.domain.transport.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransportMapper {
    /**
     * 특정 출발 지점의 배송 상태를 변경합니다.
     *
     * @param fromStoreId 출발 지점의 ID
     */
    void updateStatusByFromStoreId(int fromStoreId);

    /**
     * 특정 주문의 상품 운송 요청 상태를 변경합니다.
     *
     * @param orderId 주문 ID
     * @param statusCode 상태 코드
     */
    void updateTransportRequestStatus(@Param("orderId") String orderId, @Param("statusCode") int statusCode);

    /**
     * 여러 주문의 상품 운송 요청 상태를 일괄 변경합니다.
     *
     * @param orderIds 상태를 변경할 주문 ID 목록
     * @param statusCode 상태 코드
     */
    void batchUpdateTransportRequestStatus(@Param("orderIds") List<String> orderIds,
                                           @Param("statusCode") int statusCode);
}
