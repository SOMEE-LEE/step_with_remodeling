// user_join.js
$(document).ready(function() {
  // 사용자가 입력한 전화번호로 문자 메시지 전송하는 함수
  $('#sendSms').click(function() {
	// 전화번호 가져오기(공백 제거)
    const phoneNum = $('#phone').val().trim(); 
	// 전화번호 유효성 검증 정규식
	var regPhone = /^01[016789][0-9]{3,4}[0-9]{4}$/;
	
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
            // 인증번호가 같을 경우 모달 팝업창
            Modal.alert('인증번호가 일치합니다.');
          },
          error: function(error) {
            // 인증번호가 다를 경우 모달 팝업창
            Modal.alert('인증번호가 일치하지 않습니다.');
          }
       });
     }
  });
});