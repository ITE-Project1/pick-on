package com.ite.pickon.domain.transport.service;

import com.ite.pickon.domain.stock.dto.StockVO;
import com.ite.pickon.domain.stock.mapper.StockMapper;
import com.ite.pickon.domain.transport.TransportInformation;
import com.ite.pickon.domain.transport.TransportSchedule;
import com.ite.pickon.domain.transport.dto.TransportVO;
import com.ite.pickon.domain.transport.mapper.TransportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportServiceImpl implements TransportService {
    private final StockMapper stockMapper;
    private final TransportMapper transportMapper;

    @Override
    public TransportVO findOptimalTransportStore(String productId,
                                                 int quantity,
                                                 int storeId,
                                                 Date orderDate) {
        // 주문한 시간과 날짜를 LocalDateTime으로 변환
        LocalDateTime orderDateTime = convertToLocalDateTime(orderDate);
        long minDifference = Long.MAX_VALUE;
        TransportSchedule optimalSchedule = null;
        LocalDateTime optimalTime = null;

        // 모든 배차 시간을 순회하면서 가장 빠른 배차 시간 + 재고 조회
        // 배차시간, 운송 시간, 재고 3가지를 고려해야한다.
        List<StockVO> stockList = stockMapper.selectStockForStore(productId);
        for (StockVO stockVO : stockList) {
            if (stockVO.getStoreId() == storeId) continue;
            if (stockVO.getQuantity() < quantity) continue;

            for (TransportSchedule schedule : TransportSchedule.values()) {
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
                        optimalTime = resultDateTime;
                    }
                }
            }
        }

        // 픽업 예상 날짜와 시간 설정
        return new TransportVO(optimalSchedule.getStoreId(), optimalSchedule.getDepartureTime(), optimalTime.toLocalTime());
    }



    @Override
    @Transactional
    public void modifyTransportStatus(String orderId) {
        transportMapper.updateStatusByOrderId(orderId);
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
