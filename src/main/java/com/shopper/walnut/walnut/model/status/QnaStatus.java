package com.shopper.walnut.walnut.model.status;

public enum QnaStatus {
    ING("ING"),
    COMPLETE("COMPLETE"),
    MAIN("MAIN"),
    DENIED("DENIED");

    private final String value;

    QnaStatus(String value) {
        this.value = value;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }
}
