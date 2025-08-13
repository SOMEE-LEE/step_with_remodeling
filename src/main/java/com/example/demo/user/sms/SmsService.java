package com.example.demo.user.sms;

import jakarta.validation.Valid;

// 인터페이스로서 기능 명세만 정의(구현은 Impl에서)
public interface SmsService {
	// 문자 전송 데이터를 SmsDto로 받고 저장
	void sendAndSaveSms(SmsDto smsDto);

	// 입력한 인증번호가 SmsDto와 같은지 확인
	boolean verifyCode(SmsDto smsDto);
}
