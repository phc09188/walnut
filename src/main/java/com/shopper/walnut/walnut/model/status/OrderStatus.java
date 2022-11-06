package com.shopper.walnut.walnut.model.status;

public enum OrderStatus {
    ORDER("ORDER"),
    CANCEL("CANCEL");

    private String value;
    OrderStatus(String value) {
        this.value = value;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }
}
