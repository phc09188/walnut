package com.shopper.walnut.walnut.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_EMAIL_ALREADY_EXIST("이미 해당 이메일의 계정이 존재합니다."),
    USERID_ALREADY_EXIST("이미 존재하는 아이디입니다."),
    BRAND_ALREADY_EXIST("이미 존재하는 브랜드입니다.");

    private final String description;
}
