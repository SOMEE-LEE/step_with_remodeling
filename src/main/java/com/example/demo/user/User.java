package com.example.demo.user;


import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//클래스의 모든 필드에 대해 Getter 메서드를 자동 생성
@Getter
//클래스의 모든 필드에 대해 Setter 메서드를 자동 생성
@Setter
//파라미터가 없는 기본 생성자를 자동 생성
@NoArgsConstructor
//모든 필드를 파라미터로 받는 생성자를 자동 생성
@AllArgsConstructor
//이 클래스는 JPA 엔티티로 데이터베이스 테이블과 매핑
@Entity
//toString() 메서드를 자동 생성
@ToString
//엔티티가 매핑될 테이블 이름을 명시적으로 지정 (기본값은 클래스 이름)
@Table(name="user")
//BaseEntity의 생성 일자와 수정 일자 상속, Spring Security의 UserDetails 인터페이스를 구현
public class User extends BaseEntity implements UserDetails {  
	// Java에서 직렬화(Serialization)를 사용할 때 클래스의 버전 정보를 명시
	private static final long serialVersionUID = 1L;

	// 기본 키(PK)로 지정된 필드. 데이터베이스의 고유 식별자 역할
    @Id
	private String id;
    
    // 비밀번호: null 과 "" 과 " " 모두 허용하지 않음
    @NotBlank
    private String pw;
    
    // 휴대폰번호: null 과 "" 과 " " 모두 허용하지 않음
    @NotBlank
    private String phone;
    
    // 닉네임: null 과 "" 과 " " 모두 허용하지 않음
    @NotBlank
    private String userName;
    
    // 광고수신 동의 여부: NotBlank는 Boolean 타입엔 적용되지 않음
    @NotNull
    private boolean adAgree;

    // 사용자 닉네임 반환
	public String getUserName() {
		return userName;
	}
    
    /**
     * Spring Security의 UserDetails 인터페이스를 구현
     */
    // 권한 목록 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));  // 이 엔티티는 사용자를 저장하는 엔티티이므로 하드코딩
    }

    // 로그인 ID로 사용할 값
    @Override
    public String getUsername() {
        return id;
    }

    // 비밀번호
    @Override
    public String getPassword() {
        return pw;
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 자격 증명 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }
}
