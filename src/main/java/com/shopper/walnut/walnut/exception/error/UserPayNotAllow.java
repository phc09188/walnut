package com.shopper.walnut.walnut.exception.error;

import com.shopper.walnut.walnut.exception.BasicException;
import org.springframework.http.HttpStatus;

public class UserPayNotAllow extends BasicException {
    @Override
    public int getStatusCode() {
        return HttpStatus.CONFLICT.value();
    }

    @Override
    public String getMessage() {
        return "결제정보에 동의하지 않으셨습니다";
    }
}
