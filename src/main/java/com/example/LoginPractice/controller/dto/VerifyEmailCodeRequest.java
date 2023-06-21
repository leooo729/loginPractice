package com.example.LoginPractice.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class VerifyEmailCodeRequest {
    @NotBlank(message = "Email不可為空")
    private String email;
    @NotBlank(message = "EmailCode不可為空")
    private String emailCode;
}
