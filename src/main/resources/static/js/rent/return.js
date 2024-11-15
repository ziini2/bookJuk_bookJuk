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
        const totalLength = $('.return-check').length;
        const checkedLength = $('.return-check:checked').length;
        if (totalLength === checkedLength) {
            $('#selectAll').prop('checked', true);
        } else {
            $('#selectAll').prop('checked', false);
        }
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

    function returnDTO() {
        let returnDTO = {};
        let rentNums = [];
        const criteria = $('#criteria').val();
        const keyword = $('#keyword').val();
        const rented = $('#rented').prop("checked");
        const returned = $('#returned').prop("checked");

        // Active된 페이지 번호를 가져오고, 없는 경우 기본값 1 설정
        const page = parseInt($('.page-item.active .page-link').text()) || 1;
        const size = 10;

        $('.return-check:checked').each(function () {
            rentNums.push($(this).attr('data-rent-num'));
        });

        returnDTO = {
            criteria: criteria,
            keyword: keyword,
            rented: rented,
            returned: returned,
            page: page - 1, // Spring Data JPA는 페이지 번호가 0부터 시작하므로 -1
            size: size,
            rentNums: rentNums
        };

        return returnDTO;
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
                data: JSON.stringify(returnDTO()),
                contentType: 'application/json',
                success: function (response) {
                    renderTable(response.content);
                    renderPagination(response);
                    alert('반납 처리가 완료되었습니다.');
                },
                error: function () {
                    alert('반납 처리에 실패했습니다. 다시 시도해주세요.');
                }
            });
        }
    });
});