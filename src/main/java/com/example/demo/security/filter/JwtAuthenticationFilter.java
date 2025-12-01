package com.example.demo.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.user.User;
import com.example.demo.user.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.IOException;

/**
 * JWT 인증을 처리하는 커스텀 필터
 * - AbstractAuthenticationProcessingFilter를 상속하여 Spring Security 인증 흐름에 통합
 * - 지정된 URL로 들어오는 요청에서 JWT를 추출하고 검증하여 인증 처리
 */
@Component  // Spring이 자동으로 Bean으로 등록하게 함
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    // JWT 생성 및 검증을 담당하는 유틸 클래스
    private final JwtProvider jwtProvider;

    // 사용자 정보를 조회하는 서비스 (DB 접근)
    private final UserService userService;

    /**
     * 생성자에서 필터가 작동할 URL과 필요한 의존성을 주입받음
     * @param defaultFilterProcessesUrl 이 필터가 작동할 경로 (예: "/api/auth/token")
     * @param jwtProvider JWT 검증 유틸
     * @param userService 사용자 정보 조회 서비스
     */
    public JwtAuthenticationFilter(JwtProvider jwtProvider, UserService userService) {
        super("/api/auth/token");  // 필터가 작동할 URL 직접 지정
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    /**
     * 인증 시도 메서드
     * - 요청 헤더에서 JWT를 추출하고 검증
     * - 사용자 정보를 조회하여 Authentication 객체 생성
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

    	 // Authorization 헤더에서 JWT 추출
        String token = request.getHeader("Authorization");

        // 토큰이 없거나 형식이 잘못된 경우 예외 발생
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BadCredentialsException("Missing or invalid Authorization header");
        }
        
        token = token.substring(7); // "Bearer " 제거

        // JWT에서 사용자 정보 추출
        Claims userJwt = jwtProvider.getUserFromToken(token); // setSubject(id) 했던 값

        // 사용자 정보에서 사용자 아이디 추출
        String id = userJwt.getSubject();
        
        // 사용자 정보 조회 - Optional<User>는 orElseThrow()로 꺼내줘야 함
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 인증 객체 생성 (비밀번호, 권한은 User 객체에서 가져옴)
        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }


    /**
     * 인증 성공 시 호출되는 메서드
     * - SecurityContext에 인증 객체 저장
     * - 다음 필터로 요청 전달
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException, java.io.IOException {

        // 인증 정보를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authResult);

        // 다음 필터로 요청 전달
        chain.doFilter(request, response);
    }
}