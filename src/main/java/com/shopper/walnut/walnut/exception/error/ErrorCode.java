package com.shopper.walnut.walnut.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_EMAIL_ALREADY_EXIST("이미 해당 이메일의 계정이 존재합니다."),
    USERID_ALREADY_EXIST("이미 존재하는 아이디입니다."),
    BRAND_NOT_FOUND("해당 브랜드가 존재하지 않습니다"),
    CATEGORY_NOT_EXIST("카테고리가 없습니다"),
    BRAND_ALREADY_EXIST("이미 존재하는 브랜드입니다."),
    BRANDITEM_NOT_EXIST("물건 정보가 존재하지 않습니다."),
    ITEM_NOT_FOUND("물건 정보가 사라졌습니다."),
    USER_PAY_NOT_ALLOW("결제정보에 동의하지 않으셨습니다"),
    NOT_ENOUGH_CACHE("충전된 금액이 부족합니다."),
    CATEGORY_ALREADY_EXIST("해당 서브카테고리가 이미 존재합니다."),
    POINT_NOT_ENOUGH("포인트가 충분하지 않습니다."),
    ORDER_NOT_FOUND("주문이 없습니다."),
    ITEM_IS_EMPTY("재고가 떨어졌습니다.");

    private final String description;
}
