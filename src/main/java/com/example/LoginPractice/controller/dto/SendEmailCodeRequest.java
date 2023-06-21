package com.example.LoginPractice.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SendEmailCodeRequest {
    @NotBlank(message = "Email不可為空")
    private String email;
    @NotBlank(message = "姓名不可為空")
    private String name;
}
