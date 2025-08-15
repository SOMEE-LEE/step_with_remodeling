package com.example.demo.sms;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class SmsRepository {
	private final String PREFIX = "sms:"; // Redis 키에 사용할 접두사
	private final StringRedisTemplate stringRedisTemplate; // Redis 작업을 위한 StringRedisTemplate 객체
    // Logger 객체 생성: 현재 클래스(SignupPhoneAuthHandler)의 이름을 기반으로 로거를 초기화
    // 'log'는 이 클래스 내에서 로그 메시지를 출력할 때 사용
    // SLF4J 인터페이스를 통해 로그백(Logback) 또는 다른 로깅 구현체와 연결
    // 'private static final'로 선언함으로써 클래스 내에서 한 번만 생성되고 재사용
    private static final Logger log = LoggerFactory.getLogger(SignupPhoneAuthHandler.class);

	// SMS 인증 정보를 저장하는 메서드(void에서 boolean으로 변경)
	public boolean saveSmsCertification(SmsDto smsDto){	    
	    // 예외 처리 추가
	    try {
	        stringRedisTemplate.opsForValue()
	            .set(PREFIX + smsDto.getToNumber(), smsDto.getCertificationCode(), Duration.ofSeconds(180));
	        return true;
	    } catch (Exception e) {
	        log.error("Redis 저장 실패", e);
	        return false;
	    }
	}

	// SMS 인증 정보를 가져오는 메서드
    public String getSmsCertification(String phone){
	    return stringRedisTemplate.opsForValue().get(PREFIX + phone); // Redis에서 키에 해당하는 값(전화번호)을 가져옴
	}

	// SMS 인증 정보를 삭제하는 메서드
	public void deleteSmsCertification(String phone){
	    stringRedisTemplate.delete(PREFIX + phone); // Redis에서 해당 키/값을 삭제
	}

	// 해당 키가 존재하는지 확인하는 메서드
	public boolean hasKey(String phone){
	    return Boolean.TRUE.equals(stringRedisTemplate.hasKey(PREFIX + phone)); // Redis에서 키의 존재 여부를 확인
	}
}
