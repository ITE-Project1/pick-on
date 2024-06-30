package com.ite.pickon.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    FIND_FAIL_USER_ID(400, "존재하지 않는 유저입니다."),
    FIND_FAIL_PRODUCT_ID(400, "존재하지 않는 상품입니다."),
    FAIL_ORDER_BY_QUANTITY(400, "재고가 충분하지 않아 주문이 불가능합니다."),
    EXISTS_STOCK_AT_STORE(400, "해당 지점에 이미 상품이 존재합니다."),
    FIND_FAIL_PRODUCTS(400, "원하는 상품 목록을 불러올 수 없습니다."),
    FIND_FAIL_USERS(400, "원하는 유저 목록을 불러올 수 없습니다."),
    FIND_FAIL_ORDERS(400, "원하는 주문 목록을 불러올 수 없습니다."),
    INVALID_SESSION_ID(440, "세션이 만료되었습니다. 다시 로그인해주세요"),
    UNAUTHORIZED(401, "지정한 리소스에 대한 액세스 권한이 없습니다."),
    FAIL_TO_LOGIN(400, "로그인 정보가 일치하지 않습니다. 다시 로그인해주세요."),
    FAIL_SUBMIT_SMS(400, "메시지 발송 접수에 실패했습니다."),
    FAIL_SEND_SMS(400, "문자 메시지 전송에 실패했습니다."),
    FAIL_ORDER(400, "상품 주문에 실패했습니다."),
    FIND_FAIL_ORDER_ID(400, "존재하지 않는 주문입니다.");

    private final int status;
    private final String message;
}
