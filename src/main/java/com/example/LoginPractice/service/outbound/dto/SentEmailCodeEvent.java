package com.example.LoginPractice.service.outbound.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class SentEmailCodeEvent {
    private String email;
    private String name;
    private String emailCode;
}
