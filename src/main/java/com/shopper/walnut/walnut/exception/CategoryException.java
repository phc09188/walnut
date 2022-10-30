package com.shopper.walnut.walnut.exception;

import com.shopper.walnut.walnut.exception.error.ErrorCode;

public class CategoryException extends RuntimeException {
    private ErrorCode code;
    private String message;
    public CategoryException(ErrorCode code){
        this.code = code;
        this.message = code.getDescription();
    }
}
