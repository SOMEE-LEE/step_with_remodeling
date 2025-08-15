package com.example.demo.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

// REST API 컨트롤러로 지정-클라이언트의 요청을 처리하고 JSON 형태로 응답을 반환
@RestController
@RequestMapping("/sms") // "/sms" 경로로 들어오는 요청을 처리
public class SmsController {

    // 문자 서비스 클래스 의존성 주입
    private final SmsService smsService;

    // 생성자를 통해 SmsService를 주입받음
    public SmsController(@Autowired SmsService smsService){
        this.smsService = smsService;
    }
    
    /* 문자 전송 전에, user DB에 있는 전화번호인지 확인하는 메서드 필요*/

    /**
     * 인증번호를 생성하여 SMS로 전송하는 엔드포인트
     * @param phoneNum 클라이언트가 전달한 전화번호
     * @return 문자 전송 성공 메시지
     */
    @PostMapping("/send")
    public ResponseEntity<?> SendSMS(@RequestParam("phone") String phone){
    	// 6자리 랜덤 인증번호 생성 (100000 ~ 999999)
        String certificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000);

        // SmsDto 객체에 전화번호와 인증번호 설정
        SmsDto smsDto = new SmsDto();
        smsDto.setToNumber(phone);
        smsDto.setCertificationCode(certificationCode);

        // 문자 전송 서비스 호출
        smsService.sendSms(smsDto);

        // 성공 응답 반환
        return ResponseEntity.ok("문자를 전송했습니다.");
    }

    /**
     * 클라이언트가 입력한 인증번호를 검증하는 엔드포인트
     * @param 클라이언트가 전달한 전화번호와 인증번호
     * @return 인증 성공 또는 실패 메시지
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestParam("phone") String phone, @RequestParam("num") String num){
        // 인증번호 검증 로직 호출
    	SmsDto smsDto = new SmsDto();
    	smsDto.setToNumber(phone);
    	smsDto.setCertificationCode(num);
        boolean verify = smsService.verifyCode(smsDto);

        // 검증 결과에 따라 응답 반환
        if (verify) {
            return ResponseEntity.ok("인증이 되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증에 실패했습니다.");
        }
    }
}
