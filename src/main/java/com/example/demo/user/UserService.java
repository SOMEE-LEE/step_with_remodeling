package com.example.demo.user;

//인터페이스로서 기능 명세만 정의(구현은 UserServiceImpl에서)
public interface UserService {
	// 해당 사용자 이름을 가진 행이 존재하는지 확인
	boolean isUserNameDuplicate(String userName);
	
	// 해당 휴대폰 번호를 가진 행이 존재하는지 확인
	boolean isPhoneDuplicate(String phone);
}
