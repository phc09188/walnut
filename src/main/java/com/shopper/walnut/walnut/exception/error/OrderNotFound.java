package com.shopper.walnut.walnut.exception.error;

import com.shopper.walnut.walnut.exception.BasicException;
import org.springframework.http.HttpStatus;

public class OrderNotFound extends BasicException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "주문이 없습니다.";
    }
}
