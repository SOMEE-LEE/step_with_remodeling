package com.example.demo.user.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.user.SignupPhoneAuthHandler;


// Service의 실제 로직을 작성
@Service
public class SmsServiceImpl implements SmsService {
    private final SignupPhoneAuthHandler authHandler; // SMS 인증 유틸리티 객체
    private final SmsRepository smsRepository; // SMS 레포지토리 객체 (Redis)
    // Logger 객체 생성: 현재 클래스(SignupPhoneAuthHandler)의 이름을 기반으로 로거를 초기화
    // 'log'는 이 클래스 내에서 로그 메시지를 출력할 때 사용
    // SLF4J 인터페이스를 통해 로그백(Logback) 또는 다른 로깅 구현체와 연결
    // 'private static final'로 선언함으로써 클래스 내에서 한 번만 생성되고 재사용
    private static final Logger log = LoggerFactory.getLogger(SignupPhoneAuthHandler.class);

    // 의존성 주입
    public SmsServiceImpl(@Autowired SignupPhoneAuthHandler authHandler, SmsRepository smsRepository) {
    	this.authHandler = authHandler;
    	this.smsRepository = smsRepository;
    }

    @Override  // SmsService 인터페이스 메서드 구현: 메시지 전송
    public void sendSms(SmsDto smsDto) {
        try {
            authHandler.sendSms(smsDto); // SMS 인증 유틸리티를 사용하여, Handler에서 SMS 발송
            saveSms(smsDto);             // 성공했으니 전화번호와 인증번호 저장
        } catch (RuntimeException e) {
            // 실패 처리: 로그 남기기, 사용자에게 알림 등
            log.error("SMS 전송 실패: {}", e.getMessage());
            throw new RuntimeException("SMS 전송에 실패했습니다.");
        }
    }
    
    @Override // SmsService 인터페이스 메서드 구현: 전송한 메시지에 대한 전화번호와 인증번호 저장
    public void saveSms(SmsDto smsDto) {
    	// 전화번호와 인증 번호를 Redis에 저장
        if (!smsRepository.saveSmsCertification(smsDto)) {
            throw new RuntimeException("SMS 인증 정보 저장 실패");
        }
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
