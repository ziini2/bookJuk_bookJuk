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

    function loadRentData(page) {
        const criteria = $('#criteria').val();
        const keyword = $('#keyword').val();

        $.ajax({
            type: 'GET',
            url: '/admin/rent/search',
            data: {
                page: page - 1, // Spring Data JPA는 0부터 시작하므로 -1
                size: 10,
                criteria: criteria,
                keyword: keyword
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
            let delayPayment = 0;
            if (rent.returnDate) {
                const rentEnd = new Date(rent.rentEnd);
                const returnDate = new Date(rent.returnDate);
                const diff = returnDate - rentEnd;
                delayPayment = Math.floor(diff / (1000 * 60 * 60 * 24)) * 500;
            } else {
                const rentEnd = new Date(rent.rentEnd);
                const today = new Date();
                const diff = today - rentEnd;
                delayPayment = Math.floor(diff / (1000 * 60 * 60 * 24)) * 500;
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
                    <td>${rent.status}</td>
                </tr>`;
            $('tbody').append(tempHtml);
        });
    }

    function renderPagination(pageInfo) {
        $('#serverPagination').empty();

        // 페이지 정보가 있는지 확인
        if (!pageInfo || pageInfo.totalPages === 0) {
            console.log("페이지네이션 정보가 없습니다.");
            return; // 페이지네이션 정보가 없으면 함수 종료
        }

        const currentPage = pageInfo.currentPage + 1;
        const totalPages = pageInfo.totalPages;
        const pageGroupSize = 10; // 페이지 그룹당 페이지 수

        const startPage = Math.floor((currentPage - 1) / pageGroupSize) * pageGroupSize + 1;
        const endPage = Math.min(startPage + pageGroupSize - 1, totalPages);

        let paginationHtml = '';

        // 이전 버튼
        if (startPage > 1) {
            paginationHtml += `<a class="btn btn-secondary mx-1 page-link" data-page="${startPage - 1}">이전</a>`;
        }

        // 페이지 번호 버튼
        for (let i = startPage; i <= endPage; i++) {
            paginationHtml += `<a class="btn ${i === currentPage ? 'btn-secondary active' : 'btn-secondary mx-1'} page-link" data-page="${i}">${i}</a>`;
        }

        // 다음 버튼
        if (endPage < totalPages) {
            paginationHtml += `<a class="btn btn-secondary mx-1 page-link" data-page="${endPage + 1}">다음</a>`;
        }

        $('#serverPagination').html(paginationHtml);
    }

    // 페이지네이션 버튼에 클릭 이벤트 위임
    $('#serverPagination').on('click', '.page-link', function () {
        const page = $(this).data('page');
        loadRentData(page);
    });

    // 천단위 마다 쉼표 추가하는 함수
    let numbers = function (x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }
});
