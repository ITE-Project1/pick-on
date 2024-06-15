package com.ite.pickon.domain.transport.controller;

import com.ite.pickon.domain.transport.schedule.TransportScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transport")
public class TransportController {
    private final TransportScheduler transportScheduler;

    @GetMapping("/test")
    public void test() {
        // 현재 날짜와 시간을 가져옵니다
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7); // 시간 설정
        calendar.set(Calendar.MINUTE, 50);      // 분 설정
        calendar.set(Calendar.SECOND, 0);      // 초 설정
        calendar.set(Calendar.MILLISECOND, 0); // 밀리초 설정

        // Calendar에서 Date 객체 가져오기
        Date date = calendar.getTime();

        System.out.println(date);
        transportScheduler.changeTransportStatus("P002", 1, 1, date, "P00003");
    }

}
