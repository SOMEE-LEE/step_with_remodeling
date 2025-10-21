package com.example.demo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.filter.JwtAuthenticationFilter;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.user.UserService;

import lombok.RequiredArgsConstructor;

//Spring의 Configuration 클래스로 지정하여, 이 클래스가 설정 정보를 담고 있음을 나타냄
@Configuration
// Spring Security 설정에서 보안 기능을 활성화하고 커스터마이징할 수 있게 해주는 핵심 어노테이션
@EnableWebSecurity
public class SecurityConfig {
	/**
	 * 비밀번호 암호화를 위한 PasswordEncoder Bean 등록
	 * - BCrypt 해시 알고리즘을 사용하여 안전하게 비밀번호를 암호화함
	 
	 */
    private final JwtProvider jwtProvider;
    private final UserService userService;

    // 생성자 주입
    public SecurityConfig(JwtProvider jwtProvider, UserService userService) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    /**
     * 비밀번호 암호화를 위한 PasswordEncoder Bean 등록
     * - 회원가입 시 비밀번호 저장 및 로그인 시 비밀번호 검증에 사용됨
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager Bean 등록
     * - Spring Security의 인증 처리를 담당하는 핵심 컴포넌트
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    
    /**
     * JwtAuthenticationFilter Bean 등록
     * - 커스텀 JWT 인증 필터를 생성하고 AuthenticationManager를 명시적으로 설정
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtProvider, userService);
        filter.setAuthenticationManager(authenticationManager); // 🔥 필수 설정
        return filter;
    }
    
    /**
     * SecurityFilterChain Bean 등록
     * - HTTP 요청에 대한 보안 정책을 설정하고 필터 체인을 구성
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
	        // 세션을 사용하지 않도록 설정 (JWT 기반 인증은 무상태)
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        
        	// HTTP URL별 요청에 대한 인증 및 권한 설정
        	.authorizeHttpRequests(auth -> auth
        		.requestMatchers(
	                "/**",  // 모든 경로에 대해 인증 없이 접근 허용 (주의: 실제 서비스에서는 보안상 위험할 수 있음)
	                "/v3/api-docs/**",       // Swagger에서 사용하는 OpenAPI 명세 경로
	                "/swagger-ui/**",        // Swagger UI 관련 리소스 경로
	                "/swagger-ui.html"       // Swagger UI 진입 페이지
        		).permitAll() // 위 경로들에 대해 모두 접근 허용
        		.anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
            )
            
            // 커스텀 로그인 폼 사용 (Spring Security 제공 페이지 안 보이게)
            .formLogin(form -> form
            		.loginPage("/login") // 커스텀 로그인 페이지 경로
            		.defaultSuccessUrl("/") // 로그인 성공 시 이동할 경로
            		.failureUrl("/login?error") // 로그인 실패 시 이동할 경로
            		.permitAll()
            )

            // 커스텀 JWT 인증 필터를 Spring Security 필터 체인에 등록
            // -> 커스텀 JWT 인증 필터를 기본 로그인 필터인 UsernamePasswordAuthenticationFilter 앞에 위치시켜 JWT를 먼저 검사
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            
            // CSRF 비활성화 
            // - REST API 개발 시에는 CSRF 토큰이 필요 없기 때문에 일반적으로 비활성화함 
            // - JWT는 무상태이므로 필요 없음
            .csrf(csrf -> csrf.disable());

        // 설정된 SecurityFilterChain을 반환
        return http.build();
    }
}
