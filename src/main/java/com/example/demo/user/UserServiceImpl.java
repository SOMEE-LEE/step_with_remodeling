package com.example.demo.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;


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
    // UserRepository의 existsByUserName 메서드를 호출하여 boolean 값 반환
    @Override
    public boolean isUserNameDuplicate(String userName) {
        return userRepository.existsByUserName(userName);
    }

    // 사용자 휴대폰 번호가 중복되는지 확인하는 메서드
    // UserRepository의 existsByPhone 메서드를 호출하여 boolean 값 반환
	@Override
	public boolean isPhoneDuplicate(String phone) {
		return userRepository.existsByPhone(phone);
	}

	// 사용자 id가 중복되는지 확인하는 메서드
	// UserRepository의 existsById 메서드를 호출하여 boolean 값 반환
	@Override
	public boolean isIdDuplicate(String id) {
		return userRepository.existsById(id);
	}

	// 사용자 회원가입이 성공했는지 확인하는 메서드
	@Override
	@Transactional  // JPA에서 save(), delete(), update() 같은 DB 변경 작업은 트랜잭션 안에서 실행되어야만 실제로 반영
	public boolean isSignedUp(User user) {
	    try {
	        userRepository.save(user);
	        return true;
	    } catch (Exception e) {
	    	System.out.println(e.getStackTrace());
	        return false;
	    }
	}

	// 해당 아이디를 가진 사용자를 가져오는 메서드
	@Override
	public Optional<User> getUserById(String id) {
		return userRepository.findById(id);
	}
}
