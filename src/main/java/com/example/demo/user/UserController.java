package com.example.demo.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


//REST API 컨트롤러로 지정-클라이언트의 요청을 처리하고 JSON 형태로 응답을 반환
@RestController
@RequestMapping("/users") // "/users" 경로로 들어오는 요청을 처리
public class UserController {
	
    // UserService를 주입바기 위한 필드 선언: final 키워드로 불변성 유지 
	private final UserService userService;
	
    // 생성자를 통해 UserService를 자동 주입(생성자 주입 방식: 테스트 용이, 순환 참조 방지, 불변성 확보)
    public UserController(UserService userService) {
    	this.userService = userService;
    }
	
    // ID 중복확인 요청
    @PostMapping("/signup/check_id")
    public ResponseEntity<Boolean> checkUsername(@RequestParam("userName") String userName) {
        boolean isDuplicate = userService.isUsernameDuplicate(userName);
        return ResponseEntity.ok(isDuplicate);
    }
}
