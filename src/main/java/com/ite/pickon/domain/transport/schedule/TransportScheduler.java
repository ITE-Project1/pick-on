package com.ite.pickon.domain.transport.schedule;

import com.ite.pickon.domain.transport.dto.TransportVO;
import com.ite.pickon.domain.transport.service.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class TransportScheduler {
    private final TaskScheduler taskScheduler;
    private final TransportService transportService;
    public void changeTransportStatus(String productId,
                                      int quantity,
                                      int storeId,
                                      Date orderDate,
                                      String orderId) {
        TransportVO transportVO = transportService.findOptimalTransportStore(productId, quantity, storeId, orderDate);

        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), transportVO.getDepartureTime());
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

        //지점 연결 후 출발 배차 시간이 되면 배송중으로 상태 변경(배치) PENDING → SHIPPED
        taskScheduler.schedule(() -> transportService.modifyTransportStatus(orderId), date);
    }
}
