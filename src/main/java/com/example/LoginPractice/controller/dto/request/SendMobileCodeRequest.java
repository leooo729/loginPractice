package com.example.LoginPractice.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SendMobileCodeRequest {
    @NotBlank(message = "Email不可為空")
    private String email;
    @NotBlank(message = "Cellphone不可為空")
    private String cellphone;
}
