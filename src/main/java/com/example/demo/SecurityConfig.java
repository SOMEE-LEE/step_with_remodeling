package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 모든 경로+Swagger 및 API 문서 관련 경로 인증 없이 접근 허용
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                	"/**",  // 모든 경로
                    "/v3/api-docs/**",       // OpenAPI JSON 명세 경로
                    "/swagger-ui/**",        // Swagger UI 리소스 경로
                    "/swagger-ui.html"       // Swagger UI 진입 경로
                ).permitAll()
            )
            
            // 커스텀 로그인 폼 사용 (Spring Security 제공 페이지 안 보이게)
            .formLogin(form -> form
                    .loginPage("/login") // 커스텀 로그인 페이지 경로
                    .defaultSuccessUrl("/") // 로그인 성공 시 이동할 경로
                    .failureUrl("/login?error") // 로그인 실패 시 이동할 경로
                    .permitAll()
            )

            // CSRF는 일단 비활성화 (REST API 개발 시 편의상)
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
