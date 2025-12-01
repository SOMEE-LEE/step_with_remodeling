package com.example.demo.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.user.User;
import com.example.demo.user.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 사용자 인증을 직접 처리하는 커스텀 AuthenticationProvider(구현체를 생성)
 * - 로그인 요청 시 사용자 정보 조회 및 비밀번호 검증
 * - 인증 성공 시 Authentication 객체 반환
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    // 사용자 정보를 조회하기 위한 서비스
    private final UserService userService;
    // 비밀번호 인코더 추가
    private final PasswordEncoder passwordEncoder; 

    /**
     * 인증 로직을 수행하는 메서드
     * @param authentication 로그인 요청 정보 (ID, 비밀번호 등)
     * @return 인증된 Authentication 객체
     * @throws AuthenticationException 인증 실패 시 예외 발생
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = authentication.getName(); // 로그인 ID
        String rawPassword = authentication.getCredentials().toString(); // 입력된 비밀번호

        // 사용자 정보 조회
        User user = userService.getUserById(id)
            .orElseThrow(() -> new BadCredentialsException("사용자를 찾을 수 없습니다."));

        // 비밀번호 검증 (암호화된 비밀번호와 비교)
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // 인증 객체 생성
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
    
    /**
     * 이 Provider가 어떤 타입의 인증을 처리할 수 있는지 명시
     * @param authentication 인증 요청 타입
     * @return 지원 여부
     */
    @Override
    public boolean supports(Class<?> authentication) {
        // UsernamePasswordAuthenticationToken 타입의 인증 요청만 처리
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}