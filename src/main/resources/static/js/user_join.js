// user_join.js
$(document).ready(function() {
  $('#pwMessage').hide();  
  $('#pwMessage').text('');
  
  // 비밀번호 입력 필드의 값이 변경될 때마다 pwCheck 함수 호출
  $('#pw').on('input', function() {
      pwCheck();
  });

  // 사용자가 입력한 전화번호로 문자 메시지 전송하는 함수
  $('#sendSms').click(function() {
	// 전화번호 가져오기(공백 제거)
    const phoneNum = $('#phone').val().trim(); 
	// 전화번호 유효성 검증 정규식
	const regPhone = /^01[016789][0-9]{3,4}[0-9]{4}$/;
	
	// 전화번호를 입력하지 않았을 때
	if (!phoneNum) {
	  // alert창 대신 커스텀 모달 팝업창 추가
	  Modal.alert('전화번호를 입력해주세요.');
	  return;
	// 전화번호 유효성 검증
	} else if (!regPhone.test(phoneNum)) {
	  // alert창 대신 커스텀 모달 팝업창 추가
	  Modal.alert('잘못된 휴대전화번호 입니다.');
	} else {
		// 유효한 전화번호일 경우 ajax 통신
		// -> 페이지를 새로고침하지 않고도 필요한 데이터만 비동기적으로 주고받을 수 있음
		// DB에 저장된 번호인지 확인
		$.ajax({
		  type: 'POST',
		  url: '/users/signup/check_phone', // 서버 엔드포인트
		  data: { phone : phoneNum},
		  success: function(response) {
			// response가 true면 모달 팝업창
			if (response === true|| response === 'true') {
			     Modal.alert('중복 휴대전화번호 입니다.');
			// response가 false일 시에만 ajax 통신
			} else if (response === false || response === 'false') {
				// DB에 저장되지 않은 번호일 경우 문자 메시지 전송
				$.ajax({
				  type: 'POST',
				  url: '/sms/send', // 서버의 엔드포인트
				  data: { phone: phoneNum },
				  success: function(response) {
					// alert창 대신 커스텀 모달 팝업창 추가
					Modal.alert('인증번호가 전송되었습니다.');
				  },
				  error: function(error) {
					// alert창 대신 커스텀 모달 팝업창 추가
					Modal.alert('인증번호 전송에 실패했습니다.');
					// 콘솔에 error 출력
					console.error('인증번호 전송 중 오류 발생:', error);
				  }
				});
			}
		  }, 
		  error: function(error) {
			// 오류 발생 시 커스텀 모달 팝업창
			Modal.alert('휴대전화번호 중복 확인에 실패했습니다.');
			// 콘솔에 error 출력
			console.error('휴대전화번호 중복 확인 중 오류 발생:', error);
		  }
		});
	}
  });
  
  // 사용자가 입력한 인증번호가, 전화번호로 전송한 인증번호와 동일한지 확인하는 함수
  $('#checkNum').click(function() {
	  // 입력한 전화번호 가져오기(공백 제거)
	  const phoneNum = $('#phone').val().trim(); 
  	  // 입력한 인증번호 가져오기(공백 제거)
      const num = $('#num').val().trim(); 
	  
	  // 전화번호 입력이 안되어있을 경우 ajax 통신을 하지 않고 모달 팝업창을 띄움
	  if(!phoneNum) {
	    Modal.alert('전화번호를 입력해주세요.');
	  // 숫자 길이가 6자리가 아닐 경우 ajax 통신을 하지 않고 모달 팝업창을 띄움
	  } else if(num.length != 6) {
		Modal.alert('잘못된 인증번호입니다.');
	  // 숫자 길이가 6자리고 전화번호가 입력되었을 경우 ajax 통신 시도
	  } else if(num.length == 6 && phoneNum) {
        $.ajax({
          type: 'POST',
          url: '/sms/verify', // 서버의 엔드포인트
          data: { 
            phone: phoneNum,
            num: num 
          },
          success: function(response) {
			// 인증번호가 같을 경우 버튼 ui 변경
			$('#checkNum').text("인증확인 완료").prop("disabled", true);
			$('#sendSms').text("인증확인 완료").prop("disabled", true);
			// 휴대폰번호 입력창을 readonly로 변경 및 포커스 제거(disabled 상태에서는 폼 제출 시 해당 값이 전송되지 않음)
			$('#phone').prop('readonly', true);
			$('#phone').blur(); 
			// 인증번호 입력창을 readonly로 변경 및 포커스 제거(disabled 상태에서는 폼 제출 시 해당 값이 전송되지 않음)
			$('#num').prop('readonly', true);
			$('#num').blur(); 
            // 인증번호가 같을 경우 모달 팝업창
            Modal.alert('인증번호가 일치합니다.');
          },
          error: function(error) {
            // 인증번호가 다를 경우 모달 팝업창
            Modal.alert('인증번호가 일치하지 않습니다.');
			// 콘솔에 error 출력
			console.error('인증번호 확인 중 오류 발생:', error);
          }
       });
     }
  });
  
  // 사용자가 입력한 닉네임 유효성 검사 함수
  $('#checkNickname').click(function() {
    // 입력한 닉네임 가져오기
    const userName = $('#name').val();
	// 닉네임 형식 유효성 검사 (2~7자, 한글/영문/숫자만 허용)
	const regNickname = /^[가-힣a-zA-Z0-9]{2,7}$/;
	
    // 닉네임 입력 안 했을 경우
    if (userName === '') {
      Modal.alert('닉네임을 입력해주세요.');
      return;
    } else if (!regNickname.test(userName)) {
        Modal.alert('닉네임은 2~7자의 한글/영문/숫자만 사용할 수 있으며,<br>띄어쓰기는 불가능합니다.');
        return;
    } else {
		// 닉네임 중복 확인
		 $.ajax({
		   type: 'POST',
		   url: '/users/signup/check_name', // 서버 엔드포인트
		   data: { 
		     userName: userName,
		   },
		   success: function(response) {
		    // response가 true면 모달 팝업창
			if (response === true|| response === 'true') {
		      Modal.alert('중복 닉네임입니다.');
			// response가 false일 시 이벤트
			} else if (response === false || response === 'false') {
		      // 모달 팝업창(커스텀이라 y/n 설정 가능)
			  Modal.confirm('중복 닉네임이 없습니다. 이 닉네임을 사용하시겠습니까?',
			    function () {
			      // 버튼 UI 변경
			       $('#checkNickname').text('중복확인 완료').prop('disabled', true);
				   // 닉네임 입력창을 readonly로 변경(disabled 상태에서는 폼 제출 시 해당 값이 전송되지 않음)
				   $('#name').prop('readonly', true);
				   // 닉네임 입력창 포커스 제거
				   $('#name').blur(); 
			    },
			    function () {
			      // 아니오: 아무것도 안 함 (재전송 가능)
			    }
		      );
			}
		  },
		  error: function(error) {
		    // 모달 팝업창 및 error 출력
		    Modal.alert('닉네임 중복 확인 중 오류가 발생했습니다.');
			console.error('닉네임 중복 확인 중 오류 발생:', error);
		  }
		 });
	}
  });
  
  // 사용자가 입력한 아이디 유효성 검사 함수
  $('#checkId').click(function() {
    // 입력한 아이디 가져오기
    const id = $('#id').val();
    // 아이디 유효성 검사 (5~12, 영어와 숫자와 특수문자만 사용 가능, 띄어쓰기 사용 불가능)
    const regId = /^[a-zA-Z0-9!@#$%^&*]{5,12}$/;

    // 아이디 입력 안 했을 경우
    if (id === '') {
      Modal.alert('아이디를 입력해주세요.');
      return;
    } else if (!regId.test(id)) {
        Modal.alert('아이디에는 5~12자 영어/숫자/특수문자만 사용할 수 있으며,<br> 띄어쓰기는 불가능합니다.');
        return;
    } else {
  	// 아이디 중복 확인
  	 $.ajax({
  	   type: 'POST',
  	   url: '/users/signup/check_id', // 서버 엔드포인트
  	   data: { 
  	     id: id,
  	   },
  	   success: function(response) {
  	    // response가 true면 모달 팝업창
  		if (response === true|| response === 'true') {
  	      Modal.alert('중복 아이디입니다.');
  		// response가 false일 시 이벤트
  		} else if (response === false || response === 'false') {
  	      // 모달 팝업창(커스텀이라 y/n 설정 가능)
  		    Modal.confirm('중복 아이디가 없습니다. 이 아이디를 사용하시겠습니까?',
  		      function () {
  		        // 버튼 UI 변경
  		        $('#checkId').text('중복확인 완료').prop('disabled', true);
  			      // 아이디 입력창을 readonly로 변경(disabled 상태에서는 폼 제출 시 해당 값이 전송되지 않음)
  			      $('#id').prop('readonly', true);
  			      // 아이디 입력창 포커스 제거
  			      $('#id').blur(); 
  		      },
  		      function () {
  		        // 아니오: 아무것도 안 함 (재전송 가능)
  		      }
  	      );
  		  }
  	  },
  	  error: function(error) {
  	    // 뭐가 안 될 경우: error 출력
  	    console.error('아이디 중복 확인 중 오류 발생:', error);
  	    Modal.alert('아이디 중복 확인 중 오류가 발생했습니다.');
  	  }
  	 });
    }
  });
  
  // 비밀번호 유효성 검사
  function pwCheck() {
    // 비밀번호 값 가져오기
    const pw = $('#pw').val();
    // 비밀번호 유효성 검증 정규식: 최소 6자에서 16자까지 쓸 수 있으며, 영어 소문자, 대문자, 숫자, 특수문자가 모두 포함되어야 함
	const regPw = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{6,16}$/;
    
    // 비밀번호가 빈 칸이 아닐 때만 유효성 검사 실행
    if (pw != "") {
      if (!regPw.test(pw)) {
        $('#pwMessage').show(); 
        $('#pwMessage').html('비밀번호는 최소 6자에서 16자까지 쓸 수 있으며,<br>영어 소문자, 대문자, 숫자, 특수문자가 모두 포함되어야 합니다.');
        $('#pwMessage').css('color', 'red');
      } else {
        $('#pwMessage').show(); 
  	      $('#pwMessage').html('사용 가능한 비밀번호입니다.');
  	      $('#pwMessage').css('color', 'black');
      }
    }
  }
});