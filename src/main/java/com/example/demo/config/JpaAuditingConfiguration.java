package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing을 활성화하기 위한 설정 클래스
 * 이 설정을 통해 @CreatedDate, @LastModifiedDate 등의 어노테이션이 작동
 */
//Spring의 Configuration 클래스로 지정하여, 이 클래스가 설정 정보를 담고 있음을 나타냄
@Configuration 
//JPA Auditing 기능을 활성화하기 위한 어노테이션: 엔티티의 생성일자, 수정일자 등을 자동으로 관리
@EnableJpaAuditing 
public class JpaAuditingConfiguration {
    // 현재는 별도의 설정이 없지만, 필요 시 AuditorAware 등을 정의
}