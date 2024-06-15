package com.ite.pickon.domain.transport.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class TransportScheduler {
    private final TaskScheduler taskScheduler;

    @Scheduled(fixedDelay=10000)
    public void init() {
        System.out.println("동작여부 확인");
    }
}
