package com.example.demo.user.sms;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// DTO: 데이터를 운반만 하는 객체(추후 Redis에 추가할 것)
public class SmsDto {
	// null 과 "" 과 " " 모두 허용하지 않음
    @NotBlank
    private String toNumber;
    
    // null 과 "" 과 " " 모두 허용하지 않음
    @NotBlank
    private String certificationCode;
}