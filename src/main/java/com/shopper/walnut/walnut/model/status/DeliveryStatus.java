package com.shopper.walnut.walnut.model.status;

public enum DeliveryStatus {
    READY("READY"),
    DELIVERY_START("DELIVERY_START"),
    COMPLETE("COMPLETE"),
    CANCEL("CANCEL");

    private final String value;

    DeliveryStatus(String value) {
        this.value = value;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }
}
