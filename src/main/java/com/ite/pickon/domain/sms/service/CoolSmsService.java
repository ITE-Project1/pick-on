package com.ite.pickon.domain.sms.service;

import com.ite.pickon.exception.CustomException;
import lombok.extern.log4j.Log4j2;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.ite.pickon.exception.ErrorCode.FAIL_SEND_SMS;
import static com.ite.pickon.exception.ErrorCode.FAIL_SUBMIT_SMS;

@Service
@Log4j2
public class CoolSmsService implements SmsService {

    // CoolSMS API 키
    @Value("${coolsms.api_key}")
    private String apiKey;

    // CoolSMS API 시크릿 키
    @Value("${coolsms.api_secret}")
    private String apiSecret;

    // 발신자 전화번호
    @Value("${coolsms.from_phone}")
    private String fromPhone;

    /**
     * SMS 메시지를 전송하는 메서드
     *
     * @param toPhone 수신자 전화번호
     * @param content 메시지 내용
     */
    @Override
    public void sendSms(String toPhone, String content) {
        // 메시지 서비스 초기화
        DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "http://api.coolsms.co.kr");

        // 메시지 객체 생성
        Message message = new Message();
        message.setFrom(fromPhone.replace("-", ""));
        message.setTo(toPhone.replace("-", ""));
        message.setText(content);

        try {
            // 메시지 전송
            messageService.send(message);
        } catch (NurigoMessageNotReceivedException e) {
            // 메시지 전송 실패 시 로그 기록 및 예외 처리
            log.error(e.getFailedMessageList());
            log.error(e.getMessage());
            throw new CustomException(FAIL_SUBMIT_SMS);
        } catch (Exception e) {
            // 일반적인 예외 처리
            log.error(e.getMessage());
            throw new CustomException(FAIL_SEND_SMS);
        }
    }
}