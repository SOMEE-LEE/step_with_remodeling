package com.example.demo.user;

import org.springframework.stereotype.Service;


// UserService의 실제 로직을 작성
@Service
public class UserServiceImpl implements UserService {
	 // UserRepository를 주입받기 위한 필드 선언: 'final' 키워드를 사용하여 객체 불변성 유지
     private final UserRepository userRepository;

    // 생성자 주입 방식: 스프링이 이 생성자를 통해 UserRepository를 자동으로 주입(테스트 용이, 순환 참조 방지, 불변성 확보)
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 이름이 중복되는지 확인하는 메서드
    // UserRepository의 existsByUsername 메서드를 호출하여 boolean 값 반환
    @Override
    public boolean isUsernameDuplicate(String userName) {
        return userRepository.existsByUserName(userName);
    }
}
