package com.ite.pickon.domain.sms.template;

import org.springframework.stereotype.Component;

@Component
public class SmsMessageTemplate {
    // 주문 완료
    public String getOrderCompletionMessage(String orderId, String pickupDate, String toStore, String productName) {
        return String.format("[Pickon] 주문이 완료되었습니다.\n" +
                        "주문 번호: %s\n" +
                        "픽업 예상 날짜: %s\n" +
                        "수령 지점: %s\n" +
                        "상품명: %s\n\n" +
                        "지점별 영업 시간 확인 후 방문 부탁드립니다.",
                orderId, pickupDate, toStore, productName);
    }

    // 픽업 가능
    public String getPickUpReadyMessage(String orderId, String pickupDate, String toStore, String productName) {
        return String.format("[Pickon] 주문하신 상품 픽업 준비가 완료되었습니다.\n" +
                        "주문 번호: %s\n" +
                        "픽업 예상 날짜: %s\n" +
                        "수령 지점: %s\n" +
                        "상품명: %s\n\n" +
                        "지점별 영업 시간 확인 후 방문 부탁드립니다.",
                orderId, pickupDate, toStore, productName);
    }

}
