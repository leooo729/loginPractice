package com.example.LoginPractice.controller.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FailResponse {
    private String statusCode="9999";
    private String statusMsg="失敗";
}
