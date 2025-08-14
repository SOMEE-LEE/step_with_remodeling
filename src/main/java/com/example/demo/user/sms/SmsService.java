package com.example.demo.user.sms;

import jakarta.validation.Valid;

// 인터페이스로서 기능 명세만 정의(구현은 Impl에서)
public interface SmsService {
	// 문자 메시지를 전송
	void sendSms(SmsDto smsDto);
	
	// 전송한 문자 메시지에 대한 전화번호/인증번호 저장
	void saveSms(SmsDto smsDto);

	// 입력한 인증번호가 SmsDto와 같은지 확인
	boolean verifyCode(SmsDto smsDto);
}
