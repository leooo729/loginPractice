package com.example.LoginPractice.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class VerifyMobileCodeRequest {
    @NotBlank(message = "Cellphone不可為空")
    private String cellphone;
    @NotBlank(message = "MobileCode不可為空")
    private String mobileCode;
}
