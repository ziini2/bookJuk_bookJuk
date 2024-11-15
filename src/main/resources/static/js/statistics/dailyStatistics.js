$(document).ready(function () {
    // 매일매일 새로운 고객 수를 가져오는 함수
    $.ajax({
        type: 'GET',
        url: 'admin/daily/new-customer',
        success: function (response) {
            $('.numbers-of-new-customers').text(numbers(response));
        }
    });

    // 해당일에 진행되고 있는 이벤트 수를 가져오는 함수
    $.ajax({
        type: 'GET',
        url: 'admin/daily/event',
        success: function (response) {
            $('.numbers-of-events').text(numbers(response));
        }
    });

    // 해당일 매출(결제) 금액 가져오는 함수
    $.ajax({
        type: 'GET',
        url: 'admin/daily/revenue',
        success: function (response) {
            $('.daily-revenue').text(numbers(response));
        }
    });

    // 해당일 포인트 사용 금액 가져오는 함수
    $.ajax({
        type: 'GET',
        url: 'admin/daily/point',
        success: function (response) {
            $('.daily-point').text(numbers(response));
        }
    })

    // 해당일 총 대여 권수 가져오는 함수
    $.ajax({
        type: 'GET',
        url: 'admin/daily/rental',
        success: function (response) {
            $('.daily-rental').text(numbers(response));
        }
    })

    // 대여기간 지난 연체 권수 가져오는 함수
    $.ajax({
        type: 'GET',
        url: 'admin/daily/delay',
        success: function (response) {
            $('.daily-delay').text(numbers(response));
        }
    })

    // 천단위 마다 쉼표 추가하는 함수
    let numbers = function (x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }



});

