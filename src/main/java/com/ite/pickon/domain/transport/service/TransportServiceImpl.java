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

import java.time.LocalTime;
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
        //주문한 시간으로부터 지점별 출발 배차 시간 + 운송 시간 고려해서
        //가장 빨리 배송 받을 수 있는 지점 연결
        LocalTime orderTime = convertToLocalTime(orderDate);
        long minDifference = Long.MAX_VALUE;
        TransportSchedule optimalSchedule = null;
        LocalTime optimalTime = null;

        // 모든 배차 시간을 순회하면서 가장 빠른 배차 시간 + 재고 조회
        // 배차시간, 운송 시간, 재고 3가지를 고려해야한다.
        List<StockVO> stockList = stockMapper.selectStockForStore(productId);
        for (StockVO stockVO : stockList) {
            System.out.println(stockVO.getQuantity());
            if (stockVO.getStoreId() == storeId) continue;
            if (stockVO.getQuantity() < quantity) continue;

            for (TransportSchedule schedule : TransportSchedule.values()) {
                if (schedule.getStoreId() != stockVO.getStoreId()) continue;
                LocalTime scheduleTime = schedule.getDepartureTime();
                if (scheduleTime.toSecondOfDay() - orderTime.toSecondOfDay() < 0) continue;
                for (TransportInformation information : TransportInformation.values()) {
                    if (information.getFromStoreId() != stockVO.getStoreId() || information.getToStoreId() != storeId) {
                        continue;
                    }
                    // scheduleTime + 해당 지점까지의 운송 시간 계산
                    LocalTime resultTime = scheduleTime.plusMinutes(information.getTransportMinutes());

                    // 주문 시각으로 부터 가장 빠른 시간 구하기
                    long difference = resultTime.toSecondOfDay() - orderTime.toSecondOfDay();
                    if (difference < minDifference) {
                        minDifference = difference;
                        optimalSchedule = schedule;
                        optimalTime = resultTime;
                    }
                }
            }
        }

        //주문 테이블에 픽업 예상 날짜도 배송 도착 예정 시간 + 임의로 설정한 시간으로 고려해서 만들어줘야한다.

        return new TransportVO(optimalSchedule.getStoreId(), optimalSchedule.getDepartureTime(), optimalTime);
    }

    @Override
    @Transactional
    public void modifyTransportStatus(String orderId) {
        transportMapper.updateStatusByOrderId(orderId);
    }

    private static LocalTime convertToLocalTime(Date date) {
        return new java.sql.Time(date.getTime()).toLocalTime();
    }
}
