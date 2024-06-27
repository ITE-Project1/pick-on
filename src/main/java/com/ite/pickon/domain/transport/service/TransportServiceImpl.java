package com.ite.pickon.domain.transport.service;

import com.ite.pickon.domain.stock.dto.StockVO;
import com.ite.pickon.domain.stock.mapper.StockMapper;
import com.ite.pickon.domain.transport.TransportInformation;
import com.ite.pickon.domain.transport.TransportSchedule;
import com.ite.pickon.domain.transport.dto.TransportVO;
import com.ite.pickon.domain.transport.mapper.TransportMapper;
import com.ite.pickon.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.ite.pickon.exception.ErrorCode.FAIL_ORDER_BY_QUANTITY;

@Service
@RequiredArgsConstructor
public class TransportServiceImpl implements TransportService {
    private final StockMapper stockMapper;
    private final TransportMapper transportMapper;

    /**
     * 최적의 배송 출발 지점을 선정합니다.
     *
     * @param productId 주문한 제품의 ID
     * @param quantity 주문 수량
     * @param storeId 주문한 지점의 ID
     * @param orderDate 주문 날짜와 시간
     * @return 최적의 배송 출발 지점, 출발 시간 및 도착 시간을 담은 TransportVO 객체
     * @throws CustomException 재고가 부족하여 최적의 배송 출발 지점을 선정하지 못한 경우 예외 발생
     */
    @Override
    public TransportVO findOptimalTransportStore(String productId,
                                                 int quantity,
                                                 int storeId,
                                                 Date orderDate) {
        // 주문한 시간과 날짜를 LocalDateTime으로 변환
        LocalDateTime orderDateTime = convertToLocalDateTime(orderDate);
        long minDifference = Long.MAX_VALUE;
        TransportSchedule optimalSchedule = null;
        LocalDateTime optimalDepartureDateTime = null;
        LocalDateTime optimalArrivalDateTime = null;

        // 모든 배차 시간을 순회하면서 가장 빠른 배차 시간 + 재고 조회
        // 재고, 배차 시간, 운송 시간 총 3가지 요소를 고려
        List<StockVO> stockList = stockMapper.selectStockForStore(productId);
        for (StockVO stockVO : stockList) {
            // 동일한 지점 X
            if (stockVO.getStoreId() == storeId) continue;

            // 주문 수량보다 재고가 많아야 함
            if (stockVO.getQuantity() < quantity) continue;

            for (TransportSchedule schedule : TransportSchedule.values()) {
                // 재고가 확보된 지점과 운송 스케줄에 저장된 지점이 같은지 검증
                if (schedule.getStoreId() != stockVO.getStoreId()) continue;

                // 배차 시간을 LocalDateTime으로 변환
                LocalDateTime scheduleDateTime = LocalDateTime.of(orderDateTime.toLocalDate(), schedule.getDepartureTime());
                if (scheduleDateTime.isBefore(orderDateTime)) {
                    // 배차 시간이 주문 시간 이전이면 다음 날로 설정
                    scheduleDateTime = scheduleDateTime.plusDays(1);
                }

                for (TransportInformation information : TransportInformation.values()) {
                    if (information.getFromStoreId() != stockVO.getStoreId() || information.getToStoreId() != storeId) {
                        continue;
                    }
                    // scheduleDateTime + 해당 지점까지의 운송 시간 계산
                    LocalDateTime resultDateTime = scheduleDateTime.plusMinutes(information.getTransportMinutes());

                    // 주문 시각으로 부터 가장 빠른 시간 구하기
                    long difference = Duration.between(orderDateTime, resultDateTime).toSeconds();
                    if (difference < minDifference) {
                        minDifference = difference;
                        optimalSchedule = schedule;
                        optimalArrivalDateTime = resultDateTime;
                        optimalDepartureDateTime = scheduleDateTime;
                    }
                }
            }
        }

        // 최적의 배송 출발 지점을 선정하지 못했으면 예외 처리
        if (optimalSchedule == null) {
            throw new CustomException(FAIL_ORDER_BY_QUANTITY);
        }

        // 배송 출발 지점, 배송 출발 시간, 배송 도착 시간을 담은 VO 객체 반환
        return new TransportVO(optimalSchedule.getStoreId(), optimalDepartureDateTime, optimalArrivalDateTime);
    }

    /**
     * 배송 정보 상태를 변경합니다.
     *
     * @param fromStoreId 출발 지점의 ID
     */
    @Override
    @Transactional
    public void modifyTransportStatus(int fromStoreId) {
        transportMapper.updateStatusByFromStoreId(fromStoreId);
    }


    /**
     * Date 객체를 LocalDateTime 객체로 변환합니다.
     *
     * @param date 변환할 Date 객체
     * @return 변환된 LocalDateTime 객체
     */
    private LocalDateTime convertToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
