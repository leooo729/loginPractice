package com.example.LoginPractice.service.outbound.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class SentMobileCodeEvent {
    private String email;
    private String name;
    private String phone;
    private String emailCode;

}
