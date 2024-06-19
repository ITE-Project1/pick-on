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
    FIND_FAIL_ORDER_ID(400, "존재하지 않는 주문입니다."),
    FIND_FAIL_PRODUCTS(400, "원하는 상품 목록을 불러올 수 없습니다.");

    private final int status;
    private final String message;
}
