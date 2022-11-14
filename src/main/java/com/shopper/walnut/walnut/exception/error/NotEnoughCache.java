package com.shopper.walnut.walnut.exception.error;

import com.shopper.walnut.walnut.exception.BasicException;
import org.springframework.http.HttpStatus;

public class NotEnoughCache extends BasicException {
    @Override
    public int getStatusCode() {
        return HttpStatus.CONFLICT.value();
    }

    @Override
    public String getMessage() {
        return "충전된 금액이 부족합니다.";
    }
}
