package com.example.demo;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter // 모든 필드에 대한 getter 메서드 자동 생성
@Setter // 모든 필드에 대한 setter 메서드 자동 생성
@ToString // toString 메서드 자동 생성
@MappedSuperclass // 이 클래스를 상속받는 엔티티 클래스에 필드가 매핑되도록 지정
@EntityListeners(AuditingEntityListener.class) // 생성일-수정일 자동 설정을 위해 Auditing 기능 활성화
public class BaseEntity {

    @CreatedDate // 엔티티가 처음 저장될 때 자동으로 현재 시간이 설정됨
    @Column(updatable = false) // 생성일은 수정되지 않도록 설정
    private LocalDateTime createdAt; // 생성일시

    @LastModifiedDate // 엔티티가 수정될 때 자동으로 현재 시간이 설정됨
    private LocalDateTime updatedAt; // 최종 수정일시
}