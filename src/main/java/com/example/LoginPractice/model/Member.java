package com.example.LoginPractice.model;


import com.example.LoginPractice.controller.dto.request.SendEmailCodeRequest;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Table(name = "member")
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "email")
    private String email;

    @Column(name = "email_code")
    private String emailCode;

    @Column(name = "email_code_expire")
    private Date emailCodeExpire;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "phone")
    private String phone;

    @Column(name = "account")
    private String account;

    @Column(name = "password")
    private String password;

    @Column(name = "mobile_code")
    private String mobileCode;

    @Column(name = "mobile_code_expire")
    private Date mobileCodeExpire;

    @Column(name = "status")
    private Integer status;

    public Member(SendEmailCodeRequest request) {
        this.email = request.getEmail();
        this.name = request.getName();
    }


}
