package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// exclude 부분: DB 연결 없이 테스트
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class StepWithRemodelingApplication {

	public static void main(String[] args) {
		SpringApplication.run(StepWithRemodelingApplication.class, args);
	}

}
