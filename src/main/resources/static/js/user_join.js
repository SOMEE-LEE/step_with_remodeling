$(document).ready(function() {
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
});