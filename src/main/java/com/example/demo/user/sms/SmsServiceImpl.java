package com.example.demo.user.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.user.SignupPhoneAuthHandler;

// Service의 실제 로직을 작성
@Service
public class SmsServiceImpl implements SmsService {

    private final SignupPhoneAuthHandler authHandler;

    //의존성 주입
    public SmsServiceImpl(@Autowired SignupPhoneAuthHandler authHandler) {
        this.authHandler = authHandler;
    }

    @Override // SmsService 인터페이스 메서드 구현
    public void sendSms(String phoneNum, String certificationCode) {
        authHandler.sendSms(phoneNum, certificationCode); // SMS 인증 유틸리티를 사용하여, Handler에서 SMS 발송
    }
}
