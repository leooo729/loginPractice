package com.example.LoginPractice.service;


import com.example.LoginPractice.config.CheckErrorException;
import com.example.LoginPractice.controller.dto.request.*;
import com.example.LoginPractice.controller.dto.response.*;
import com.example.LoginPractice.model.Member;
import com.example.LoginPractice.service.enums.CheckStatusEnum;
import com.example.LoginPractice.service.enums.MemberStatusEnum;
import com.example.LoginPractice.repository.MemberRepository;
import com.example.LoginPractice.service.outbound.EventPublisher;
import com.example.LoginPractice.service.outbound.dto.MemberCommittedEvent;
import com.example.LoginPractice.service.outbound.dto.SentEmailCodeEvent;
import com.example.LoginPractice.service.outbound.dto.SentMobileCodeEvent;
import com.example.LoginPractice.service.utils.GenerateVerificationCodeUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final EventPublisher eventPublisher;

    public SendEmailCodeResponse sendEmailCode(SendEmailCodeRequest request) {
        SendEmailCodeResponse response = new SendEmailCodeResponse();
        Member member;

        //透過Mail 查找Member
        Optional<Member> optionalMember = memberRepository.findByEmail(request.getEmail());
        //Mail已存在 檢核Status跟驗證碼是否過期
        if (optionalMember.isPresent()) {
            //checkValid
            this.checkSendEmail(optionalMember.get());

            //a5.狀態為已傳送Mail(0) 且驗證碼已過期
            member = optionalMember.get();
            response.setStatusMsg(CheckStatusEnum.EMAIL_CODE_EXPIRED.getStatusMsg());

        } else {
            //資料庫無此Email註冊帳號 創一新帳號
            member = new Member(request);
        }

        member.setEmailCode(GenerateVerificationCodeUtil.MakeVerificationCode(20));
        member.setEmailCodeExpire(makeExpireTime());
        member.setStatus(MemberStatusEnum.SEND_EMAIL_CODE.getStatusCode());

        memberRepository.save(member);
        //發送Email驗證碼事件
        SentEmailCodeEvent event = new SentEmailCodeEvent(member.getEmail(), member.getName(), member.getEmailCode());
        eventPublisher.publish(event);

        return response;
    }

    public VerifyEmailCodeResponse verifyEmailCode(VerifyEmailCodeRequest request) {
        VerifyEmailCodeResponse response = new VerifyEmailCodeResponse();
        //透過Mail 查找Member
        Optional<Member> optionalMember = memberRepository.findByEmail(request.getEmail());

        if (optionalMember.isPresent()) {
            //checkValid
            this.checkVerifyEmailCode(optionalMember.get());
            //check c1.檢核Email驗證碼是否正確
            this.checkEmailCodeValid(optionalMember.get(), request.getEmailCode());

            optionalMember.get().setStatus(MemberStatusEnum.VERIFIED_EMAIL_CODE.getStatusCode());
            memberRepository.save(optionalMember.get());

        } else {
            throw new CheckErrorException(CheckStatusEnum.EMAIL_NOT_FOUND);
        }
        return response;
    }

    public SendMobileCodeResponse sendMobileCode(SendMobileCodeRequest request) {
        SendMobileCodeResponse response = new SendMobileCodeResponse();
        //透過Mail 查找Member
        Optional<Member> optionalMemberByEmail = memberRepository.findByEmail(request.getEmail());
        //透過Phone 查找Member
        Optional<Member> optionalMemberByPhone = memberRepository.findByPhone(request.getCellphone());

        if (optionalMemberByEmail.isPresent()) {
            //checkValid
            this.checkSendMobile(optionalMemberByEmail.get());
            //a4.檢核手機是否已存在
            if (optionalMemberByPhone.isPresent()) {
                this.checkPhoneExist(optionalMemberByEmail.get(), request.getCellphone());
            }
            //檢核待驗證的手機已申請未通過(2) 驗證碼已過期
            if (this.checkMobileCodeExpire(optionalMemberByEmail.get())) {
                response.setStatusMsg(CheckStatusEnum.PHONE_CODE_RESEND.getStatusMsg());
            }
        } else {
            throw new CheckErrorException(CheckStatusEnum.EMAIL_NOT_FOUND);
        }

        Member member = optionalMemberByEmail.get();
        member.setPhone(request.getCellphone());
        member.setMobileCode(GenerateVerificationCodeUtil.MakeVerificationCode(6));
        member.setMobileCodeExpire(makeExpireTime());
        member.setStatus(MemberStatusEnum.SEND_MOBILE_CODE.getStatusCode());
        memberRepository.save(member);
        //發送手機驗證碼事件
        SentMobileCodeEvent event = new SentMobileCodeEvent(member.getEmail(), member.getName(), member.getPhone(), member.getMobileCode());
        eventPublisher.publish(event);

        return response;
    }

    public VerifyMobileCodeResponse verifyMobileCode(VerifyMobileCodeRequest request) {
        VerifyMobileCodeResponse response = new VerifyMobileCodeResponse();
        //透過手機 查找Member
        Optional<Member> optionalMember = memberRepository.findByPhone(request.getCellphone());

        if (optionalMember.isPresent()) {
            //checkValid
            this.checkVerifyMobileCode(optionalMember.get());
            //check c1.檢核手機驗證碼是否正確
            this.checkMobileCode(optionalMember.get(), request.getMobileCode());

            optionalMember.get().setStatus(MemberStatusEnum.VERIFIED_MOBILE_CODE.getStatusCode());
            memberRepository.save(optionalMember.get());

        } else {
            throw new CheckErrorException(CheckStatusEnum.PHONE_NOT_FOUND);
        }
        return response;
    }

    public CommitRegistryResponse commitRegistryInfo(CommitRegistryRequest request) {
        CommitRegistryResponse response = new CommitRegistryResponse();
        //透過Mail跟手機 查找Member
        Optional<Member> optionalMember = memberRepository.findByEmailAndPhone(request.getEmail(), request.getCellphone());
        //確定有此註冊資料 將Request資料傳入
        if (optionalMember.isPresent()) {

            Member member = optionalMember.get();
            member.setName(request.getName());
            member.setAddress(request.getAddress());
            member.setBirthday(request.getBirthday());
            member.setPhone(request.getCellphone());
            member.setAccount(request.getAccount());
            member.setPassword(request.getPassword());
            member.setStatus(MemberStatusEnum.FINISHED_REGISTRY.getStatusCode());

            memberRepository.save(member);

            response.setId(member.getId());
            //發送註冊完成事件
            MemberCommittedEvent event = new MemberCommittedEvent(member.getEmail(), member.getName(), member.getPhone());
            eventPublisher.publish(event);

        } else {
            throw new CheckErrorException(CheckStatusEnum.REGISTRATION_DATA_NOT_FOUND);
        }

        return response;
    }

    //------------------------------------------------------------------------------Method
    //產生驗證碼過期時間
    private Date makeExpireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        return calendar.getTime();
    }
    //------------------------------------------------------------------------------multiple Check

    private void checkSendEmail(Member member) {

        // a1.狀態為已傳送手機號碼(2) （此處不需判斷讚證碼是否過期） 或是 狀態為已驗證完Mail(1)
        this.checkSendMobileCodeStatusAndVerifiedEmailCodeStatus(member);

        // a3.狀態為已完成Mail跟手機的驗證 只差基本資料填寫(3) / a4.狀態為已完成Mail跟手機的驗證 基本資料也填寫完畢(4)
        this.checkVerifiedMobileCodeStatusAndFinishedRegistryStatus(member);

        // a6.狀態為已傳送Mail驗證碼(0) 且驗證碼尚未過期
        if (member.getStatus().equals(MemberStatusEnum.SEND_EMAIL_CODE.getStatusCode()) && !this.checkEmailCodeExpire(member)) {
            throw new CheckErrorException(CheckStatusEnum.EMAIL_CODE_NOT_EXPIRED);
        }
    }

    private void checkVerifyEmailCode(Member member) {

        //a1.狀態為碼已傳送Mail(0) 且驗證碼已過期
        if (this.checkEmailCodeExpire(member))
            throw new CheckErrorException(CheckStatusEnum.EMAIL_CODE_EXPIRED);

        //b1.狀態為已傳送手機號碼(2) （此處不需判斷驗證碼是否過期） 或是 狀態為已驗證完Mail(1)
        this.checkSendMobileCodeStatusAndVerifiedEmailCodeStatus(member);

        //b2.狀態為已完成Mail跟手機的驗證 只差基本資料填寫(3) / b3.狀態為已完成Mail跟手機的驗證 基本資料也填寫完畢(4)
        this.checkVerifiedMobileCodeStatusAndFinishedRegistryStatus(member);
    }

    private void checkSendMobile(Member member) {

        //狀態為已傳送Mail驗證碼(0) 檢核Email驗證碼是否過期
        if (member.getStatus().equals(MemberStatusEnum.SEND_EMAIL_CODE.getStatusCode()))
            throw new CheckErrorException(this.checkEmailCodeExpire(member) ? CheckStatusEnum.EMAIL_CODE_EXPIRED : CheckStatusEnum.EMAIL_CODE_NOT_EXPIRED);

        //a3.狀態為已完成Mail跟手機的驗證 只差基本資料填寫(3) / a5.狀態為已完成Mail跟手機的驗證 基本資料也填寫完畢(4)
        this.checkVerifiedMobileCodeStatusAndFinishedRegistryStatus(member);

    }

    private void checkVerifyMobileCode(Member member) {

        //a1.驗證碼已過期
        if (member.getStatus().equals(MemberStatusEnum.SEND_MOBILE_CODE.getStatusCode())
                && member.getMobileCodeExpire().before(new Date())) {
            throw new CheckErrorException(CheckStatusEnum.PHONE_CODE_EXPIRED);
        }

        //b2.狀態為已完成Mail跟手機的驗證 只差基本資料填寫(3) / b3.狀態為已完成Mail跟手機的驗證 基本資料也填寫完畢(4)
        this.checkVerifiedMobileCodeStatusAndFinishedRegistryStatus(member);
    }

    //------------------------------------------------------------------------------Check

    private void checkVerifiedMobileCodeStatusAndFinishedRegistryStatus(Member member) {
        // 狀態為已完成Mail跟手機的驗證 只差基本資料填寫(3)
        this.checkVerifiedMobileCodeStatus(member);
        // 狀態為已完成Mail跟手機的驗證 基本資料也填寫完畢(4)
        this.checkFinishedRegistryStatus(member);
    }

    // check Email驗證碼是否過期 過期回傳 true
    private boolean checkEmailCodeExpire(Member member) {
        return member.getEmailCodeExpire().before(new Date());
    }

    //檢核Email驗證碼是否正確
    private void checkEmailCodeValid(Member member, String emailCode) {
        if (!member.getEmailCode().equals(emailCode))
            throw new CheckErrorException(CheckStatusEnum.EMAIL_CODE_INCORRECT);
    }

    // 檢核狀態為 已傳送手機號碼(2) 或是 狀態為已驗證Email(1)
    private void checkSendMobileCodeStatusAndVerifiedEmailCodeStatus(Member member) {
        if (member.getStatus().equals(MemberStatusEnum.SEND_MOBILE_CODE.getStatusCode())
                || member.getStatus().equals(MemberStatusEnum.VERIFIED_EMAIL_CODE.getStatusCode())) {
            throw new CheckErrorException(CheckStatusEnum.EMAIL_COMPLETED_TO_PHONE);
        }
    }

    //檢核待驗證的手機已申請未通過(2) 驗證碼已過期
    private boolean checkMobileCodeExpire(Member member) {
        //檢核待驗證的手機已申請 但尚未認證
        if (member.getStatus().equals(MemberStatusEnum.SEND_MOBILE_CODE.getStatusCode())) {
            if (member.getMobileCodeExpire().before(new Date())) {  //a6.驗證碼已過期
                return true;
            } //a7.驗證碼未過期
            throw new CheckErrorException(CheckStatusEnum.PHONE_CODE_NOT_EXPIRED);
        }
        return false;
    }

    // 檢核手機是否已存在
    private void checkPhoneExist(Member member, String phone) {
        if (!phone.equals(member.getPhone()))
            throw new CheckErrorException(CheckStatusEnum.PHONE_ALREADY_REGISTERED);
    }

    //檢核手機驗證碼是否正確
    private void checkMobileCode(Member member, String mobileCode) {
        if (!member.getMobileCode().equals(mobileCode))
            throw new CheckErrorException(CheckStatusEnum.PHONE_CODE_INCORRECT);
    }

    // 狀態為已完成Mail跟手機的驗證 只差基本資料填寫(3)
    private void checkVerifiedMobileCodeStatus(Member member) {
        if (member.getStatus().equals(MemberStatusEnum.VERIFIED_MOBILE_CODE.getStatusCode()))
            throw new CheckErrorException(CheckStatusEnum.REGISTRATION_COMPLETED_TO_INFO);
    }

    // 狀態為已完成Mail跟手機的驗證 基本資料也填寫完畢(4)
    private void checkFinishedRegistryStatus(Member member) {
        if (member.getStatus().equals(MemberStatusEnum.FINISHED_REGISTRY.getStatusCode()))
            throw new CheckErrorException(CheckStatusEnum.REGISTRATION_COMPLETED_TO_LOGIN);
    }
}
