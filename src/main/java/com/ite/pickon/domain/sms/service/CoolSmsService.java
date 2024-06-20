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
    @Value("${coolsms.api_key}")
    private String apiKey;

    @Value("${coolsms.api_secret}")
    private String apiSecret;

    @Value("${coolsms.from_phone}")
    private String fromPhone;

    @Override
    public void sendSms(String toPhone, String content) {

        log.info("API Key: " + apiKey); // 로그로 API 키 확인
        log.info("API Secret: " + apiSecret); // 로그로 API 시크릿 확인
        log.info("From Phone: " + fromPhone); // 로그로 발신번호 확인

        DefaultMessageService messageService =  NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "http://api.coolsms.co.kr");
        Message message = new Message();
        message.setFrom(fromPhone.replace("-", ""));
        message.setTo(toPhone.replace("-", ""));
        message.setText(content);

        try {
            messageService.send(message);
        } catch (NurigoMessageNotReceivedException e) {
            log.error("=================1================");
            log.error(e.getFailedMessageList());
            log.error(e.getMessage());
            e.printStackTrace();
            throw new CustomException(FAIL_SUBMIT_SMS);
        } catch (Exception e) {
            log.error("=================2================");
            log.error(e.getMessage());
            e.printStackTrace();
            throw new CustomException(FAIL_SEND_SMS);
        }
    }
}
