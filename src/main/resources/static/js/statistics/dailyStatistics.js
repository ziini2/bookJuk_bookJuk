$(document).ready(function () {
    // 매일매일 새로운 고객 수를 가져오는 함수
    $.ajax({
        type: 'GET',
        url: '/daily/new-customer',
        success: function (response) {
            $('.numbers-of-new-customers').text(response);
        }
    });

    // 해당일에 진행되고 있는 이벤트 수를 가져오는 함수
    $.ajax({
        type: 'GET',
        url: '/daily/event',
        success: function (response) {
            $('.numbers-of-events').text(response);
        }
    });


});

