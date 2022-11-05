package com.shopper.walnut.walnut.model.status;

public interface UserStatus {
    /**
     * 가입 요청 중 (이메일 인증이 안 된 상태)
     */
    String MEMBER_STATUS_REQ = "REQ";
    /**
     * 정상 회원
     */
    String MEMBER_STATUS_ING = "ING";

    /**
     * 현재 정지 처리된 상태
     */
    String MEMBER_STATUS_STOP = "STOP";

    /**
     * 탈퇴된 회원
     */
    String MEMBER_STATUS_WITHDRAW = "WITHDRAW";

}
