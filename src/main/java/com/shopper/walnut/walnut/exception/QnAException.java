package com.shopper.walnut.walnut.exception;

import com.shopper.walnut.walnut.exception.error.ErrorCode;

public class QnAException extends RuntimeException{
    private ErrorCode code;
    private String errorMessage;

    public QnAException(ErrorCode errorCode) {
        this.code = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
