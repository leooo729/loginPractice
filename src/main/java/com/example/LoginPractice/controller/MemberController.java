package com.example.LoginPractice.controller;

import com.example.LoginPractice.controller.dto.request.*;
import com.example.LoginPractice.controller.dto.response.*;
import com.example.LoginPractice.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/send/email-code/v1")
    public SendEmailCodeResponse sendEmailCode(@RequestBody @Valid SendEmailCodeRequest request) {
        SendEmailCodeResponse response = memberService.sendEmailCode(request);
        return response;
    }

    @PostMapping("/verify/email-code/v1")
    public VerifyEmailCodeResponse verifyEmailCode(@RequestBody @Valid VerifyEmailCodeRequest request) {
        VerifyEmailCodeResponse response = memberService.verifyEmailCode(request);
        return response;
    }

    @PostMapping("/send/mobile-code/v1")
    public SendMobileCodeResponse sendMobileCode(@RequestBody @Valid SendMobileCodeRequest request) {
        SendMobileCodeResponse response = memberService.sendMobileCode(request);
        return response;
    }

    @PostMapping("/verify/mobile-code/v1")
    public VerifyMobileCodeResponse sendMobileCode(@RequestBody @Valid VerifyMobileCodeRequest request) {
        VerifyMobileCodeResponse response = memberService.verifyMobileCode(request);
        return response;
    }

    @PostMapping("/commit/registry/v1")
    public CommitRegistryResponse commitRegistryInfo(@RequestBody @Valid CommitRegistryRequest request) {
        CommitRegistryResponse response = memberService.commitRegistryInfo(request);
        return response;
    }
}
