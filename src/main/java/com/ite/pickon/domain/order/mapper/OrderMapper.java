package com.ite.pickon.domain.order.mapper;

import com.ite.pickon.domain.order.dto.MultiOrderRes;
import com.ite.pickon.domain.order.dto.OrderReq;
import com.ite.pickon.domain.order.dto.OrderRes;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderMapper {
    // 주문 생성
    void insertOrder(@Param("userId") Long userId,
                     @Param("orderReq") OrderReq orderReq,
                     @Param("pickupDate") LocalDateTime pickupDate);

    // 지점 간 운송 요청 생성
    void insertTransportRequest(@Param("orderReq") OrderReq orderReq,
                                @Param("fromStoreId") int fromStoreId);

    // 주문 목록 조회
    List<MultiOrderRes> selectOrderListByStore(@Param("storeId") String storeId,
                                               @Param("offset") int offset,
                                               @Param("pageSize") int pageSize,
                                               @Param("keyword") String keyword);

    // 주문 상세 조회
    OrderRes selectOrderById(@Param("orderId") String orderId);

    // 주문 상태 변경
    int updateOrderStatus(@Param("orderId") String orderId, @Param("statusCode") int statusCode);

    // 주문 상태 일괄 변경
    void batchUpdateOrderStatus(@Param("orderIds") List<String> orderIds, @Param("statusCode") int statusCode);

}
