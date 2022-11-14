package com.shopper.walnut.walnut.exception.error;

import com.shopper.walnut.walnut.exception.BasicException;
import org.springframework.http.HttpStatus;

public class CategoryAlreadyExist extends BasicException {
    @Override
    public int getStatusCode() {
        return HttpStatus.CONFLICT.value();
    }

    @Override
    public String getMessage() {
        return "이미 존재하는 카테고리입니다.";
    }
}
