package com.ite.pickon.domain.transport.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransportMapper {
    void updateStatusByFromStoreId(int fromStoreId);

    // 상품 운송 요청 상태 변경
    void updateTransportRequestStatus(@Param("orderId") String orderId, @Param("statusCode") int statusCode);

    // 상품 운송 요청 상태 일괄 변경
    void batchUpdateTransportRequestStatus(@Param("orderIds") List<String> orderIds,
                                           @Param("statusCode") int statusCode);
}
