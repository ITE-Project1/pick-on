package com.ite.pickon.domain.sms.service;

import com.ite.pickon.config.EnvConfig;
import com.ite.pickon.exception.CustomException;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.ite.pickon.exception.ErrorCode.FAIL_SEND_SMS;
import static com.ite.pickon.exception.ErrorCode.FAIL_SUBMIT_SMS;

@Service
public class CoolSmsService implements SmsService {

    // CoolSMS API 키
    private final String apiKey = EnvConfig.getEnv("COOLSMS_API_KEY");

    // CoolSMS API 시크릿 키
    private final String apiSecret = EnvConfig.getEnv("COOLSMS_API_SECRET");

    // 발신자 전화번호
    private final String fromPhone = EnvConfig.getEnv("COOLSMS_FROM_PHONE");

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
            // 메시지 전송 실패
            throw new CustomException(FAIL_SUBMIT_SMS);
        } catch (Exception e) {
            // 일반적인 예외 처리
            throw new CustomException(FAIL_SEND_SMS);
        }
    }
}