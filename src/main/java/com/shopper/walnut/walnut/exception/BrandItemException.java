package com.shopper.walnut.walnut.exception;

import com.shopper.walnut.walnut.exception.error.ErrorCode;

public class BrandItemException extends RuntimeException {
    private ErrorCode code;
    private String errorMessage;

    public BrandItemException(ErrorCode errorCode) {
        this.code = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
