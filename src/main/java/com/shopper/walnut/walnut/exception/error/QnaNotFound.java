package com.shopper.walnut.walnut.exception.error;

import com.shopper.walnut.walnut.exception.BasicException;
import org.springframework.http.HttpStatus;

public class QnaNotFound extends BasicException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "QnA가 없습니다.";
    }
}
