package com.ite.pickon.domain.order.mapper;

import com.ite.pickon.domain.order.dto.MultiOrderResponse;
import com.ite.pickon.domain.order.dto.MyOrderResponse;
import com.ite.pickon.domain.order.dto.OrderRequest;
import com.ite.pickon.domain.order.dto.OrderResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderMapper {

    // 주문 생성 및 지점 간 운송 요청 생성
    void insertOrderAndRequest(OrderRequest orderRequest);

    // 지점별 주문 목록 조회
    List<MultiOrderResponse> selectOrderListByStore(@Param("storeId") int storeId,
                                                    @Param("pageable") Pageable pageable,
                                                    @Param("keyword") String keyword);

    // 지점별 주문 목록 페이지 갯수 조회
    int countTotalOrderPages(@Param("storeId") int storeId,
                             @Param("keyword") String keyword,
                             @Param("pageSize") int pageSize);

    // 나의 주문 목록 조회
    List<MyOrderResponse> selectMyOrderList(@Param("userId") Long userId, @Param("pageable") Pageable pageable);

    // 나의 주문 목록 페이지 갯수 조회
    int countTotalOrderBasePages(@Param("userId") Long userId, @Param("pageSize") int pageSize);

    // 주문 상세 조회
    OrderResponse selectOrderById(@Param("orderId") String orderId);

    // 주문 상태 변경
    int updateOrderStatus(@Param("orderId") String orderId, @Param("statusCode") int statusCode);

    // 주문 상태 일괄 변경
    void batchUpdateOrderStatus(@Param("orderIds") List<String> orderIds, @Param("statusCode") int statusCode);
}
