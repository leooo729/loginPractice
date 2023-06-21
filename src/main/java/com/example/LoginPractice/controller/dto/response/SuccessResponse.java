package com.example.LoginPractice.controller.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponse {
    private String statusCode = "0000";
    private String statusMsg = "成功";
}
