package com.ite.pickon.domain.order.mapper;

import com.ite.pickon.domain.order.dto.MultiOrderResponse;
import com.ite.pickon.domain.order.dto.OrderRequest;
import com.ite.pickon.domain.order.dto.OrderResponse;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderMapper {
    // 주문 생성
    void insertOrder(@Param("userId") Long userId,
                     @Param("orderRequest") OrderRequest orderRequest,
                     @Param("pickupDate") LocalDateTime pickupDate);

    // 지점 간 운송 요청 생성
    void insertTransportRequest(@Param("orderRequest") OrderRequest orderRequest,
                                @Param("fromStoreId") int fromStoreId);

    // 주문 목록 조회
    List<MultiOrderResponse> selectOrderListByStore(@Param("storeId") int storeId,
                                                    @Param("offset") int offset,
                                                    @Param("pageSize") int pageSize,
                                                    @Param("keyword") String keyword);

    // 전체 주문 목록 페이지 갯수 조회
    int countTotalOrderPages(@Param("storeId") int storeId,
                             @Param("keyword") String keyword,
                             @Param("pageSize") int pageSize);

    // 주문 상세 조회
    OrderResponse selectOrderById(@Param("orderId") String orderId);

    // 주문 상태 변경
    int updateOrderStatus(@Param("orderId") String orderId, @Param("statusCode") int statusCode);

    // 주문 상태 일괄 변경
    void batchUpdateOrderStatus(@Param("orderIds") List<String> orderIds, @Param("statusCode") int statusCode);

}
