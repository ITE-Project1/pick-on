package com.ite.pickon.domain.transport.schedule;

import com.ite.pickon.domain.transport.TransportSchedule;
import com.ite.pickon.domain.transport.service.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class TransportScheduler {
    private final TransportService transportService;

    @Scheduled(cron = "0 0 8 * * *") // 매일 08:00
    public void scheduleTaskForStore1() {
        transportService.modifyTransportStatus(TransportSchedule.STORE_1.getStoreId());
    }

    @Scheduled(cron = "0 0 9 * * *") // 매일 09:00
    public void scheduleTaskForStore2() {
        transportService.modifyTransportStatus(TransportSchedule.STORE_2.getStoreId());
    }

    @Scheduled(cron = "0 0 10 * * *") // 매일 10:00
    public void scheduleTaskForStore3() {
        transportService.modifyTransportStatus(TransportSchedule.STORE_3.getStoreId());
    }

    @Scheduled(cron = "0 0 11 * * *") // 매일 11:00
    public void scheduleTaskForStore4() {
        transportService.modifyTransportStatus(TransportSchedule.STORE_4.getStoreId());
    }

    @Scheduled(cron = "0 0 12 * * *") // 매일 12:00
    public void scheduleTaskForStore5() {
        transportService.modifyTransportStatus(TransportSchedule.STORE_5.getStoreId());
    }

}
