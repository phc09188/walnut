package com.shopper.walnut.walnut.exception;

import com.shopper.walnut.walnut.exception.error.ErrorCode;

public class UserRegisterException extends RuntimeException{
    private ErrorCode errorCode;
    private String errorMessage;

    public UserRegisterException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
