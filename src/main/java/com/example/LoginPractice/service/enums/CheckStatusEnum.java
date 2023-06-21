package com.example.LoginPractice.service.enums;

import lombok.Getter;

@Getter
public enum CheckStatusEnum {
    EMAIL_COMPLETED_TO_PHONE("9001", "您的電子信箱已申請完成,重新導向發送手機驗證碼頁面。"),
    REGISTRATION_COMPLETED_TO_INFO("9003", "您的註冊驗證已完成,重新導向建立基本資料頁面。"),
    REGISTRATION_COMPLETED_TO_LOGIN("9004", "您的註冊驗證已完成,重新導向登入頁面。"),
    EMAIL_CODE_NOT_EXPIRED("9005", "驗證碼尚未過期，請至電子信箱收取驗證碼。"),
    EMAIL_NOT_FOUND("9006", "查無此電子信箱。"),
    EMAIL_CODE_EXPIRED("9007", "您的電子信箱驗證碼已過期,請重新發送驗證碼。"),
    EMAIL_CODE_INCORRECT("9008", "您的電子信箱驗證碼錯誤,請重新發送驗證碼。"),
    PHONE_ALREADY_REGISTERED("9010", "您的手機已被申請過，請重新輸入新手機。"),
    PHONE_CODE_NOT_EXPIRED("9011", "驗證碼尚未過期，請至手機收取驗證碼。"),
    PHONE_CODE_EXPIRED("9013", "您的手機驗證碼已過期,請重新發送驗證碼。"),
    PHONE_CODE_INCORRECT("9014", "您的手機驗證碼錯誤,請重新發送驗證碼。"),
    REGISTRATION_DATA_NOT_FOUND("9015", "查無此註冊資料，請重新確認輸入資料。"),
    PHONE_NOT_FOUND("xxxx", "查無此手機資料。"),
    PHONE_CODE_RESEND("xxxx","已重新發送手機驗證碼。")
    ;

    private String statusCode;
    private String statusMsg;

    CheckStatusEnum(String statusCode, String statusMsg) {
        this.statusCode=statusCode;
        this.statusMsg = statusMsg;
    }
}
