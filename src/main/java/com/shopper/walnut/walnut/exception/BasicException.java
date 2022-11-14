package com.shopper.walnut.walnut.exception;

public abstract class BasicException extends RuntimeException{
    abstract public int getStatusCode();
    abstract public String getMessage();
}
