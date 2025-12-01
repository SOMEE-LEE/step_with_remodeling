package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.example.demo.security.config.SecurityConfig;

@SpringBootTest
@Import(SecurityConfig.class)  // SecurityConfig를 명시적으로 로딩(PasswordEncoder 포함된 설정 명시적으로 로딩)
class StepWithRemodelingApplicationTests {

	@Test
	void contextLoads() {
	}

}
