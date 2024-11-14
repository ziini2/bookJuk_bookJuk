$(document).ready(function () {

    // #selectAll 체크박스를 클릭하면 모든 .return-check 체크박스의 상태를 변경
    $('#selectAll').click(function () {
        const isChecked = $(this).prop('checked');
        $('.return-check').prop('checked', isChecked);
        calculateTotal();
    });

    //.return-check 체크박스 상태 변경을 감지하여 #selectAll 상태 업데이트
    $('body').on('change', '.return-check', function () {
        allChecked();
        calculateTotal();
    });

    function allChecked() {

    }

    function calculateTotal() {
        let total = 0;
        $('.return-check:checked').each(function () {
            total += parseInt($(this).attr('data-late-fee'));
        });

        $('#totalLateFee').text(numbers(total));

    }

    let numbers = function (x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    function rentNums() {
        let rentNums = [];
        $('.return-check:checked').each(function () {
            rentNums.push($(this).attr('data-rent-num'));
        });
        return rentNums;
    }

    $('#returnButton').click(function () {
        if ($('.return-check:checked').length === 0) {
            alert('반납할 대여 목록을 선택해주세요.');
            return;
        }
        if (!confirm('선택한 대여 목록을 반납 처리하시겠습니까?')) {
            return;
        } else {
            $.ajax({
                type: 'POST',
                url: '/admin/rent/return',
                data: JSON.stringify(rentNums()),
                contentType: 'application/json',
                success: function (response) {
                    alert('반납 처리가 완료되었습니다.');
                },
                error: function () {
                    alert('반납 처리에 실패했습니다. 다시 시도해주세요.');
                }
            });
        }
    });
});