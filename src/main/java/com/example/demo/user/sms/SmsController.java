package com.example.demo.user.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.v3.oas.annotations.parameters.RequestBody;

// 웹 요청을 받아서 적절한 처리를 하고 JSON, XML 같은 데이터 반환
@RestController
@RequestMapping("/sms")
public class SmsController {

	private final SmsService smsService;

	public SmsController(@Autowired SmsService smsService){
        this.smsService = smsService;
    }
	
    @PostMapping("/send")
    public ResponseEntity<?> SendSMS(@RequestBody @RequestParam("phone") String phoneNum){
    	String certificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000); // 6자리 인증번호 생성  
    	smsService.sendSms(phoneNum, certificationCode);
        return ResponseEntity.ok("문자를 전송했습니다.");
    }
}
