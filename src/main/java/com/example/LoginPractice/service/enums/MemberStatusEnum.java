package com.example.LoginPractice.service.enums;

import lombok.Getter;


public enum MemberStatusEnum {

    SEND_EMAIL_CODE(0),
    VERIFIED_EMAIL_CODE(1),
    SEND_MOBILE_CODE(2),
    VERIFIED_MOBILE_CODE(3),
    FINISHED_REGISTRY(4);

    @Getter
    private Integer statusCode;

    MemberStatusEnum(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
