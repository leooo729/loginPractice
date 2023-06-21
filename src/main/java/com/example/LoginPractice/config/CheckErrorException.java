package com.example.LoginPractice.config;

import com.example.LoginPractice.service.enums.CheckStatusEnum;
import lombok.Getter;

@Getter
public class CheckErrorException extends RuntimeException {
    private String statusCode;
    private String statusMsg;

    public CheckErrorException(CheckStatusEnum errorInfo) {
        super();
        this.statusCode = errorInfo.getStatusCode();
        this.statusMsg = errorInfo.getStatusMsg();
    }
}
