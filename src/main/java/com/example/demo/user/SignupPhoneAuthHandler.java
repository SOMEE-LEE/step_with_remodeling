package com.example.demo.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;


// 해당 클래스를 Spring의 컨테이너에 Bean으로 등록하고 관리하게 함
@Component
public class SignupPhoneAuthHandler {
	@Value("${coolsms.apikey}") // coolsms의 API 키 주입
	private String apikey;

	@Value("${coolsms.apisecret}") // coolsms의 API 비밀키 주입
	private String apisecret;

	@Value("${coolsms.fromnumber}") // coolsms의 발신자 번호 주입
	private String fromnumber;

	// 메시지 서비스를 위한 객체
	DefaultMessageService messageService; 
	
	// 의존성 주입이 완료된 후 초기화를 수행하는 메서드
	@PostConstruct 
    public void init(){
		// 메시지 서비스 초기화
        this.messageService = NurigoApp.INSTANCE.initialize(apikey, apisecret, "https://api.solapi.com"); 
    }

	
	// 단문 문자(SMS) 발송
	public void sendSms(String toNumber, String certificationCode) {		
		// 새 메시지 객체 생성
		Message message = new Message();
	    // 발신자 번호 설정 
		message.setFrom(fromnumber);
	    // 수신자 번호 설정
	    message.setTo(toNumber);
	    // 메시지 내용 설정(인증번호 6자리: 랜덤생성)
	    message.setText("[스텝 위드 Step With] 본인확인 인증번호 ["+certificationCode+"]를 입력해 주세요.");

	    try {
	    	// 메시지 발송 요청
	        messageService.send(message);
	    } catch (NurigoMessageNotReceivedException e) {
	    	// 발송에 실패한 메시지 목록 확인
	        System.out.println(e.getFailedMessageList());
	        System.out.println(e.getMessage());
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
	}
}
