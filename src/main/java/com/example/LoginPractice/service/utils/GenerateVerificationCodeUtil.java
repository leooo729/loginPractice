package com.example.LoginPractice.service.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class GenerateVerificationCodeUtil {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    static Random random = new Random();

    public static String MakeVerificationCode(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}

//    public static String MakeVerificationMobileCode(int length) {
//        StringBuilder sb = new StringBuilder(length);
//
//        for (int i = 0; i < length; i++) {
//            int randomNumber = random.nextInt(10);
//            sb.append(randomNumber);
//        }
//        return sb.toString();
//
//    }