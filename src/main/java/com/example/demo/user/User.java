package com.example.demo.user;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
public class User {
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
    
    // 광고수신 동의 여부: null 과 "" 과 " " 모두 허용하지 않음
    @NotBlank
    private boolean adAgree;
}
