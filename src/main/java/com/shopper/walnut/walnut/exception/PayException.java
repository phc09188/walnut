package com.shopper.walnut.walnut.exception;

import com.shopper.walnut.walnut.exception.error.ErrorCode;

public class PayException extends RuntimeException{
    private ErrorCode errorCode;
    private String errorMessage;

    public PayException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
