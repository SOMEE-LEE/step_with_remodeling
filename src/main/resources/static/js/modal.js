// 커스텀 모달 팝업창을 함수로 만들어, 필요 시 호출
// 즉시 실행 함수(IIFE)를 사용하여 jQuery를 안전하게 전달하고, 모듈화된 구조 생성
// let: 변할 수 있는 값을 선언
let Modal = (function($) {
    // 모달 관련 DOM 요소를 저장할 변수 선언
    let modal;
    let modalMessage;
    let confirmButton;
    let cancelButton;

    // 모달을 초기화하고 DOM에 추가하는 함수
    function init() {
        // 모달이 아직 생성되지 않았다면
        if (!modal) {
            // 모달 HTML을 body에 동적으로 추가
            $('body').append(`
                <div id="custom_modal">
                    <div id="custom_modal_content">
                        <p id="modalMessage"></p>
                        <div class="btn_wrap">
                            <button id="confirmButton">확인</button>
                            <button id="cancelButton">취소</button>
                        </div>
                    </div>
                </div>
            `);
            
            // 각 요소를 jQuery 객체로 캐싱
            modal = $('#custom_modal');
            modalMessage = $('#modalMessage');
            confirmButton = $('#confirmButton');
            cancelButton = $('#cancelButton');
        }
    }

    // 모달을 보여주는 함수
    function show(options) {
        init(); // 모달이 없으면 초기화

        // 메시지 설정
        modalMessage.html(options.message);
        
        // 취소 버튼 표시 여부에 따라 버튼 텍스트 및 표시 설정
        if (options.showCancel) {
            confirmButton.text(options.confirmText || '예');
            cancelButton.text(options.cancelText || '아니오').show();
        } else {
            confirmButton.text(options.confirmText || '확인');
            cancelButton.hide();
        }

        // 확인 버튼 클릭 이벤트 설정
        confirmButton.off('click').on('click', function() {
            hide(); // 모달 닫기
            if (options.onConfirm) options.onConfirm(); // 콜백 실행
        });

        // 취소 버튼 클릭 이벤트 설정
        cancelButton.off('click').on('click', function() {
            hide(); // 모달 닫기
            if (options.onCancel) options.onCancel(); // 콜백 실행
        });

        // 모달 표시
        modal.css('display', 'flex');
    }

    // 모달을 숨기는 함수
    function hide() {
        if (modal) modal.css('display', 'none');
    }

    // 외부에서 사용할 수 있는 API 반환
    return {
        // 확인 버튼만 있는 알림 모달
        alert: function(message, onConfirm) {
            show({
                message: message,
                showCancel: false,
                onConfirm: onConfirm
            });
        },
        // 확인/취소 버튼이 있는 확인 모달
        confirm: function(message, onConfirm, onCancel) {
            show({
                message: message,
                showCancel: true,
                onConfirm: onConfirm,
                onCancel: onCancel
            });
        },
        // 커스텀 옵션으로 모달 호출
        custom: show,
        // 외부에서 모달 숨기기
        hide: hide
    };
})(jQuery);