package com.shopper.walnut.walnut.model.status;

public enum EventStatus {
    REQ("승인 대기 중"),
    COMPLETE("승인 완료"),
    CANCEL("이벤트 취소");

    private final String value;

    EventStatus(String value) {
        this.value = value;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }
}
