package com.example.LoginPractice.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CommitRegistryRequest {
    @NotBlank(message = "email不可為空")
    private String email;

    @NotBlank(message = "name不可為空")
    private String name;

    @NotBlank(message = "address不可為空")
    private String address;

    @NotBlank(message = "birthday不可為空")
    @Pattern(regexp = "^$|(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])", message = "該birthday欄位格式不正確,請輸入正確格式。 ex.20001105")
    private String birthday;

    @NotBlank(message = "cellphone不可為空")
    @Pattern(regexp = "^$|\\d{10}", message = "該cellphone欄位格式不正確,請輸入10碼數字。")
    private String cellphone;

    private String account;

    @NotBlank(message = "password不可為空")
    @Pattern(regexp = "^$|[a-zA-Z0-9]+", message = "該password欄位格式不正確,只能包含數字跟英文。")
    private String password;

}
