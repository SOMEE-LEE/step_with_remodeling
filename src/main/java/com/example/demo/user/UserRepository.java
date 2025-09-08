package com.example.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// User 엔티티에 대한 CRUD + customMethod 사용 가능
public interface UserRepository extends JpaRepository<User, String> {
	// 해당 사용자 이름을 가진 행이 존재하는지 boolean으로 반환
	boolean existsByUserName(String userName);
}
