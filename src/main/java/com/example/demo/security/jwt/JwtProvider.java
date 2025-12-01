package com.example.demo.security.jwt;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.user.User;
import com.example.demo.user.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

/**
 * JWT 토큰을 생성하는 클래스
 * - JJWT 라이브러리를 사용하며, 사용자 정보를 기반으로 서명된 토큰 생성 및 파싱
 */
//Spring 컨테이너에 Bean으로 등록되어 의존성 주입이 가능하게 함
@Component 
public class JwtProvider {
	/**@RequiredArgsConstructor 없이 직접 생성자를 작성**/
	// JWT 서명을 위한 비밀 키 
    private final String secretKey;

    // 토큰 유효 시간 (24시간 = 1000ms * 60s * 60m * 24h)
    private final long expirationMs = 1000 * 60 * 60 * 24;

    // 사용자 정보 기반 JWT 토큰 생성, 검증, 재발급 과정에서 사용자 정보를 활용하기 위함
    private final UserService userService;

    // application.properties에서 생성자로 직접 주입
    public JwtProvider(@Value("${jwt.secret}") String secretKey, UserService userService) {
        this.secretKey = secretKey;
        this.userService = userService;
    }

    /**
     * JWT 토큰을 생성
     * @param id 사용자 식별 정보 (관리자와 구분)
     * @return 생성된 JWT 문자열
     */
    public String createToken(User user) {
        Date now = new Date(); // 현재 시간
        Date expiry = new Date(now.getTime() + expirationMs); // 만료 시간 계산

        // JWT 생성
        return Jwts.builder()
            .setSubject(user.getUsername()) // 토큰의 주체(subject) 설정: 사용자 식별자
            .claim("name", user.getUserName())     // 사용자 이름
            .claim("role", "user")     // 사용자 권한(user 고정)
            .setIssuedAt(now)        // 토큰 발급 시간
            .setExpiration(expiry)   // 토큰 만료 시간
            .signWith(               // 서명 알고리즘 및 키 설정
                    Keys.hmacShaKeyFor(secretKey.getBytes()), // HMAC SHA 키 생성
                    SignatureAlgorithm.HS256                  // 서명 알고리즘: HMAC-SHA256
                )
                .compact(); // 최종적으로 JWT 문자열로 압축
    }

    /**
     * JWT에서 사용자 식별 정보 추출
     * @param token JWT 문자열
     * @return Claims 객체 (subject, name, role 등 포함)
     */
    public Claims getUserFromToken(String token) {
        // JWT 파서 빌더를 생성하고, 서명 키를 설정
        return Jwts.parserBuilder()
            // JWT 서명 검증을 위한 키 설정 (secretKey를 바이트 배열로 변환하여 사용)
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
            
            // 파서 빌더를 통해 JWT 파서 객체 생성
            .build()
            
            // JWT 문자열을 파싱하여 서명 검증 및 클레임 추출
            .parseClaimsJws(token)
            
            // JWT의 본문(Claims) 객체를 가져옴
            .getBody();
    }
    
    /**
     * JWT 재발급
     * @param oldToken 기존 JWT
     * @return 새로 발급된 JWT
     */
    public String refreshToken(String oldToken) {
        Claims claims = getUserFromToken(oldToken);
        String id = claims.getSubject();

       // 사용자 정보 재조회 - Optional<User>는 orElseThrow()로 꺼내줘야 함
        User user = userService.getUserById(id)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 새 토큰 생성
        return createToken(user);
    }
}