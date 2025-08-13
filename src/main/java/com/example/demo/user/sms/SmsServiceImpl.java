package com.example.demo.user.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.user.SignupPhoneAuthHandler;


// Service의 실제 로직을 작성
@Service
public class SmsServiceImpl implements SmsService {
    private final SignupPhoneAuthHandler authHandler; // SMS 인증 유틸리티 객체
    private final SmsRepository smsRepository; // SMS 레포지토리 객체 (Redis)

    // 의존성 주입
    public SmsServiceImpl(@Autowired SignupPhoneAuthHandler authHandler, SmsRepository smsRepository) {
    	this.authHandler = authHandler;
    	this.smsRepository = smsRepository;
    }

    @Override  // SmsService 인터페이스 메서드 구현: 메시지 전송과 동시에 Redis에 저장
    public void sendAndSaveSms(SmsDto smsDto) {
        authHandler.sendSms(smsDto); // SMS 인증 유틸리티를 사용하여, Handler에서 SMS 발송
        smsRepository.saveSmsCertification(smsDto); // 인증 코드를 Redis에 저장
    }

	@Override  // SmsService 인터페이스 메서드 구현: 인증번호 확인
	public boolean verifyCode(SmsDto smsDto) {
        if (isVerify(smsDto.getToNumber(), smsDto.getCertificationCode())) { // 인증 코드 검증
            smsRepository.deleteSmsCertification(smsDto.getToNumber()); // 검증이 성공하면 Redis에서 인증 코드 삭제
            return true; // 인증 성공 반환
        } else {
            return false; // 인증 실패 반환
        }
	}
	
    // 전화번호와 인증 코드를 검증하는 메서드
    public boolean isVerify(String phone, String certificationCode) {
        return smsRepository.hasKey(phone) && // 전화번호에 대한 키가 존재하고
               smsRepository.getSmsCertification(phone).equals(certificationCode); // 저장된 인증 코드와 입력된 인증 코드가 일치하는지 확인
    }
}
