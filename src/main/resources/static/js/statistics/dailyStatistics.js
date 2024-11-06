/* jquery로 document ready일 때 함수 시작*/
$(document).ready(function () {
    // 매일매일 새로운 고객 수를 가져오는 함수
    $.ajax({
        type: 'GET',
        url: '/daily/new-customer',
        success: function (response) {
            $('.numbers-of-new-customers').text(response);
        }
    });
});