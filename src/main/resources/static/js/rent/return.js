$(document).ready(function () {

    // rent-status 테이블 값에 체크박스 태그 추가
    $('#rent-status').each(function () {
        const status = $(this).text();
        if (status === '대여 중') {
            $(this).html('<input type="checkbox" class="return-check" data-rent-id="' + $(this).attr('data-rent-id') + '">');
        }
    });


})