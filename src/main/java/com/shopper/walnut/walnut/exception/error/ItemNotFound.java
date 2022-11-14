package com.shopper.walnut.walnut.exception.error;

import com.shopper.walnut.walnut.exception.BasicException;
import org.springframework.http.HttpStatus;

public class ItemNotFound extends BasicException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "물건 정보가 사라졌습니다.";
    }
}
