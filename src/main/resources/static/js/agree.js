$(document).ready(function() {
    // 이용약관 동의(필수) 모달
    $('#modal_require').on('click', function() {
        $('#require_modal').css('display', 'flex');
    });

    // 개인정보 수집/이용 동의(필수) 모달
    $('#modal_personal').on('click', function() {
        $('#personal_modal').css('display', 'flex');
    });

    // 광고성 정보 수신 동의(선택) 모달
    $('#modal_ad').on('click', function() {
        $('#ad_modal').css('display', 'flex');
    });

    // 모달 닫기 (약관 확인 버튼)
    $('#require_modal .confirm').on('click', function() {
        $('#require_modal').css('display', 'none');
    });

    $('#personal_modal .confirm').on('click', function() {
        $('#personal_modal').css('display', 'none');
    });

    $('#ad_modal .confirm').on('click', function() {
        $('#ad_modal').css('display', 'none');
    });
});
