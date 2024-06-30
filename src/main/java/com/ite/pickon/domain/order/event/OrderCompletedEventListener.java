package com.ite.pickon.domain.order.event;

import com.ite.pickon.domain.sms.service.SmsService;
import com.ite.pickon.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.ite.pickon.exception.ErrorCode.FAIL_SEND_SMS;

@Component
@RequiredArgsConstructor
public class OrderCompletedEventListener {
    private final SmsService smsService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCompletedEvent(OrderCompletedEvent event) {
        try {
            // SMS 발송
            smsService.sendSms(event.getUserPhoneNumber(), event.getMessage());
        } catch (Exception e) {
            throw new CustomException(FAIL_SEND_SMS);
        }
    }
}
