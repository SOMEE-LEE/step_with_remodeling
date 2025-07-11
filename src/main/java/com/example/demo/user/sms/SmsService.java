package com.example.demo.user.sms;


// 인터페이스로서 기능 명세만 정의
public interface SmsService {
	void sendSms(String phoneNum, String certificationCode);

	//void sendSms(SmsDto smsDto);
}
