package com.shopper.walnut.walnut.model.type;

public enum QnaType {
    PAY("결제"),
    BRAND_SIGN("입점"),
    CART("장바구니"),
    SIGN("회원가입");

    private String value;
    QnaType(String value) {
        this.value = value;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }
}
