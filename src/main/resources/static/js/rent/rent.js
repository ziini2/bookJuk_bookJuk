$(document).ready(function () {

    // Enter 키로 폼이 제출되는 것을 막기
    $('#keyword').on('keypress', function (e) {
        if (e.which === 13) { // Enter 키 코드
            e.preventDefault();
            loadRentData(1); // 검색 시 첫 페이지 로드
        }
    });

    // 페이지 로드 시 전체 대여 목록 불러오기
    loadRentData(1); // 초기 로드 시 첫 페이지 요청

    $('#searchButton').click(function () {
        loadRentData(1); // 검색 시 첫 페이지 로드
    });

    // 대여 상태에 대한 체크박스 제어: 하나는 항상 체크되도록
    $('#rented, #returned').on('change', function () {
        if (!$('#rented').prop('checked') && !$('#returned').prop('checked')) {
            $(this).prop('checked', true); // 아무 것도 체크되지 않은 경우, 마지막으로 클릭한 항목을 다시 체크 상태로 변경
        }
        loadRentData(1); // 체크박스 변경 시 첫 페이지 로드
    });

    function loadRentData(page) {
        const criteria = $('#criteria').val();
        const keyword = $('#keyword').val();
        const rented = $('#rented').prop("checked"); // 체크 여부 확인
        const returned = $('#returned').prop("checked");

        $.ajax({
            type: 'GET',
            url: '/admin/rent/search',
            data: {
                page: page - 1, // Spring Data JPA는 0부터 시작하므로 -1
                size: 10,
                criteria: criteria,
                keyword: keyword,
                rented: rented,
                returned: returned
            },
            success: function (response) {
                renderTable(response.content);
                renderPagination(response);
            },
            error: function () {
                alert("검색에 실패했습니다. 다시 시도해주세요.");
            }
        });
    }

    function renderTable(data) {
        $('tbody').empty();
        data.forEach(rent => {
            const rentStart = rent.rentStart ? rent.rentStart.toString() : '미등록';
            const rentEnd = rent.rentEnd ? rent.rentEnd.toString() : '미등록';
            const returnDate = rent.returnDate ? rent.returnDate.toString() : '미등록';
            let rentStatus;



            let delayPayment = 0;
            if (rent.returnDate) {
                const rentEndDate = new Date(rent.rentEnd);
                const returnDateObj = new Date(rent.returnDate);
                const diff = returnDateObj - rentEndDate;
                delayPayment = Math.floor(diff / (1000 * 60 * 60 * 24)) * 500;
            } else {
                const rentEndDate = new Date(rent.rentEnd);
                const today = new Date();
                const diff = today - rentEndDate;
                delayPayment = (Math.floor(diff / (1000 * 60 * 60 * 24)) + 1 ) * 500;
            }

            if (rent.returnDate) {
                rentStatus = "반납 완료";
            } else {
                rentStatus = `대여 중 <input type="checkbox" class="return-check" 
                            data-rent-num="${rent.rentNum}" data-late-fee="${delayPayment}">`;
            }



            const tempHtml = `
                <tr>
                    <td>${rent.rentNum}</td>
                    <td>${rent.userId}</td>
                    <td>${rent.userName}</td>
                    <td>${rent.userPhone}</td>
                    <td>${rent.bookNum}</td>
                    <td>${rent.bookName}</td>
                    <td>${rentStart}</td>
                    <td>${rentEnd}</td>
                    <td>${returnDate}</td>
                    <td>${numbers(delayPayment)}</td>
                    <td class="rent-status">${rentStatus}</td>
                </tr>`;
            $('tbody').append(tempHtml);
        });
    }

    function renderPagination(pageInfo) {
        $('#serverPagination').empty();

        if (!pageInfo || pageInfo.totalPages === 0) {
            console.log("페이지네이션 정보가 없습니다.");
            return;
        }

        const currentPage = pageInfo.currentPage + 1;
        const totalPages = pageInfo.totalPages;
        const pageGroupSize = 10;

        const startPage = Math.floor((currentPage - 1) / pageGroupSize) * pageGroupSize + 1;
        const endPage = Math.min(startPage + pageGroupSize - 1, totalPages);

        let paginationHtml = '';

        if (startPage > 1) {
            paginationHtml += `<a class="btn btn-secondary mx-1 page-link" data-page="${startPage - 1}">이전</a>`;
        }

        for (let i = startPage; i <= endPage; i++) {
            paginationHtml += `<a class="btn ${i === currentPage ? 'btn-secondary active' : 'btn-secondary mx-1'} page-link" data-page="${i}">${i}</a>`;
        }

        if (endPage < totalPages) {
            paginationHtml += `<a class="btn btn-secondary mx-1 page-link" data-page="${endPage + 1}">다음</a>`;
        }

        $('#serverPagination').html(paginationHtml);
    }

    $('#serverPagination').on('click', '.page-link', function () {
        const page = $(this).data('page');
        loadRentData(page);
    });

    let numbers = function (x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }


});
