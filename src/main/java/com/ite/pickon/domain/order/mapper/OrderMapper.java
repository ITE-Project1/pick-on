package com.ite.pickon.domain.order.mapper;

import com.ite.pickon.domain.order.dto.MultiOrderResponse;
import com.ite.pickon.domain.order.dto.MyOrderResponse;
import com.ite.pickon.domain.order.dto.OrderRequest;
import com.ite.pickon.domain.order.dto.OrderResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderMapper {

    /**
     * 주문 생성 및 지점 간 운송 요청 생성
     *
     * @param orderRequest 주문 요청 객체
     */
    void insertOrderAndRequest(OrderRequest orderRequest);

    /**
     * 지점별 주문 목록 조회
     *
     * @param storeId 지점 ID
     * @param pageable 페이징 정보
     * @param keyword 검색 키워드
     * @return 지점별 주문 목록
     */
    List<MultiOrderResponse> selectOrderListByStore(@Param("storeId") int storeId,
                                                    @Param("pageable") Pageable pageable,
                                                    @Param("keyword") String keyword);

    /**
     * 지점별 주문 목록 페이지 갯수 조회
     *
     * @param storeId 지점 ID
     * @param keyword 검색 키워드
     * @param pageSize 페이지 크기
     * @return 전체 페이지 수
     */
    int countTotalOrderPages(@Param("storeId") int storeId,
                             @Param("keyword") String keyword,
                             @Param("pageSize") int pageSize);

    /**
     * 나의 주문 목록 조회
     *
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 나의 주문 목록
     */
    List<MyOrderResponse> selectMyOrderList(@Param("userId") Long userId, @Param("pageable") Pageable pageable);

    /**
     * 나의 주문 목록 페이지 갯수 조회
     *
     * @param userId 사용자 ID
     * @param pageSize 페이지 크기
     * @return 전체 페이지 수
     */
    int countTotalOrderBasePages(@Param("userId") Long userId, @Param("pageSize") int pageSize);

    /**
     * 주문 상세 조회
     *
     * @param orderId 주문 ID
     * @return 주문 상세 정보
     */
    OrderResponse selectOrderById(@Param("orderId") String orderId);

    /**
     * 주문 상태 변경
     *
     * @param orderId 주문 ID
     * @param statusCode 상태 코드
     * @return 업데이트된 행 수
     */
    int updateOrderStatus(@Param("orderId") String orderId, @Param("statusCode") int statusCode);

    /**
     * 주문 상태 일괄 변경
     *
     * @param orderIds 주문 ID 목록
     * @param statusCode 상태 코드
     */
    void batchUpdateOrderStatus(@Param("orderIds") List<String> orderIds, @Param("statusCode") int statusCode);

    /**
     * 배송 완료된 주문 목록 조회
     *
     * @param orderIds 주문 ID 목록
     * @return 배송 완료된 주문 목록
     */
    List<OrderResponse> selectOrderListById(@Param("orderIds") List<String> orderIds);
}
