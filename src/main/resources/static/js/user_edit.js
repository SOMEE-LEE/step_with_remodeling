$(document).ready(function() {
  const max = 2;

  $('input[name="favorite_ield"]').on('change', function() {
    const checkedCount = $('input[name="favorite_ield"]:checked').length;

    if (checkedCount > max) {
        // 추후 모달팝업으로 대체
        alert(`최대 ${max}개까지만 선택할 수 있어요!`);
        $(this).prop('checked', false);
    }
  });
});
