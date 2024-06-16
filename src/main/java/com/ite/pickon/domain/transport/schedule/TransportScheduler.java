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

    @Scheduled(cron = "0 30 8 * * *") // 매일 08:30
    public void scheduleTaskForStore1_1() {
        transportService.modifyTransportStatus(TransportSchedule.STORE1_1.getStoreId());
    }

    @Scheduled(cron = "0 30 12 * * *") // 매일 12:30
    public void scheduleTaskForStore1_2() {
        transportService.modifyTransportStatus(TransportSchedule.STORE1_2.getStoreId());
    }

    @Scheduled(cron = "0 30 15 * * *") // 매일 15:30
    public void scheduleTaskForStore1_3() {
        transportService.modifyTransportStatus(TransportSchedule.STORE1_3.getStoreId());
    }

    @Scheduled(cron = "0 30 17 * * *") // 매일 17:30
    public void scheduleTaskForStore1_4() {
        transportService.modifyTransportStatus(TransportSchedule.STORE1_4.getStoreId());
    }

    @Scheduled(cron = "0 0 8 * * *") // 매일 08:00
    public void scheduleTaskForStore2_1() {
        transportService.modifyTransportStatus(TransportSchedule.STORE2_1.getStoreId());
    }

    @Scheduled(cron = "0 0 11 * * *") // 매일 11:00
    public void scheduleTaskForStore2_2() {
        transportService.modifyTransportStatus(TransportSchedule.STORE2_2.getStoreId());
    }

    @Scheduled(cron = "0 0 14 * * *") // 매일 14:00
    public void scheduleTaskForStore2_3() {
        transportService.modifyTransportStatus(TransportSchedule.STORE2_3.getStoreId());
    }

    @Scheduled(cron = "0 0 16 * * *") // 매일 16:00
    public void scheduleTaskForStore2_4() {
        transportService.modifyTransportStatus(TransportSchedule.STORE2_4.getStoreId());
    }

    @Scheduled(cron = "0 0 9 * * *") // 매일 09:00
    public void scheduleTaskForStore3_1() {
        transportService.modifyTransportStatus(TransportSchedule.STORE3_1.getStoreId());
    }

    @Scheduled(cron = "0 0 12 * * *") // 매일 12:00
    public void scheduleTaskForStore3_2() {
        transportService.modifyTransportStatus(TransportSchedule.STORE3_2.getStoreId());
    }

    @Scheduled(cron = "0 0 15 * * *") // 매일 15:00
    public void scheduleTaskForStore3_3() {
        transportService.modifyTransportStatus(TransportSchedule.STORE3_3.getStoreId());
    }

    @Scheduled(cron = "0 0 18 * * *") // 매일 18:00
    public void scheduleTaskForStore3_4() {
        transportService.modifyTransportStatus(TransportSchedule.STORE3_4.getStoreId());
    }

    @Scheduled(cron = "0 0 9 * * *") // 매일 09:00
    public void scheduleTaskForStore4_1() {
        transportService.modifyTransportStatus(TransportSchedule.STORE4_1.getStoreId());
    }

    @Scheduled(cron = "0 0 11 * * *") // 매일 11:00
    public void scheduleTaskForStore4_2() {
        transportService.modifyTransportStatus(TransportSchedule.STORE4_2.getStoreId());
    }

    @Scheduled(cron = "0 0 13 * * *") // 매일 13:00
    public void scheduleTaskForStore4_3() {
        transportService.modifyTransportStatus(TransportSchedule.STORE4_3.getStoreId());
    }

    @Scheduled(cron = "0 0 16 * * *") // 매일 16:00
    public void scheduleTaskForStore4_4() {
        transportService.modifyTransportStatus(TransportSchedule.STORE4_4.getStoreId());
    }

    @Scheduled(cron = "0 40 8 * * *") // 매일 08:40
    public void scheduleTaskForStore5_1() {
        transportService.modifyTransportStatus(TransportSchedule.STORE5_1.getStoreId());
    }

    @Scheduled(cron = "0 40 11 * * *") // 매일 11:40
    public void scheduleTaskForStore5_2() {
        transportService.modifyTransportStatus(TransportSchedule.STORE5_2.getStoreId());
    }

    @Scheduled(cron = "0 0 14 * * *") // 매일 14:00
    public void scheduleTaskForStore5_3() {
        transportService.modifyTransportStatus(TransportSchedule.STORE5_3.getStoreId());
    }

    @Scheduled(cron = "0 0 17 * * *") // 매일 17:00
    public void scheduleTaskForStore5_4() {
        transportService.modifyTransportStatus(TransportSchedule.STORE5_4.getStoreId());
    }

}
