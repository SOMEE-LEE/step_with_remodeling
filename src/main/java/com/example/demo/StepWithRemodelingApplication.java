package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

// Swagger가 문서 내에서 기본 서버 주소를 HTTPS가 아닌 HTTP로 강제 고정
// -> Spring Boot 앱은 기본적으로 HTTP만 제공하므로 SSL 연결 처리 불가능하기 때문
@OpenAPIDefinition(
	servers = {
			@Server(url = "http://localhost:8080", description = "Local Server")
    }
)

// exclude 부분: DB 연결 없이 테스트
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class StepWithRemodelingApplication {

	public static void main(String[] args) {
		SpringApplication.run(StepWithRemodelingApplication.class, args);
	}

}
