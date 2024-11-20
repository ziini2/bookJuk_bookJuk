$(document).ready(function () {
    let startDate = '1000-01-01';
    let endDate = '1000-01-01';
    let pointOption = $('#salesItem').text().trim();
    let storeName = $('#branch').text().trim();
    let genre = $('#genre').text().trim();
    let page = 0;  // 현재 페이지 번호 (0부터 시작)
    let size = 20; // 페이지당 항목 수

    // 테이블 데이터 로드 함수
    const populateTable = function () {
        $.ajax({
            url: '/admin/statistics/revenue',
            method: 'GET',
            data: {
                startDate: startDate,
                endDate: endDate,
                pointOption: pointOption,
                storeName: storeName,
                genre: genre,
                page: page,
                size: size
            },
            success: function (data) {
                let tableBody = $('tbody');  // tbody 선택자 수정
                tableBody.empty();

                // 데이터가 없는 경우 처리
                if (data.content.length === 0) {
                    tableBody.append('<tr><td colspan="12" class="text-center">조회된 데이터가 없습니다.</td></tr>');
                } else {
                    for (let i = 0; i < data.content.length; i++) {
                        const item = data.content[i];
                        const tempHtml = `
                            <tr>
                                <td>${data.totalElements - page * size - i}</td>
                                <td>${item.storeName}</td>
                                <td>${item.pointPayName}</td>
                                <td>${item.pointPrice}</td>
                                <td>${item.rentStart}</td>
                                <td>${item.rentEnd}</td>
                                <td>${item.returnDate}</td>
                                <td>${item.overdueDays}</td>
                                <td>${item.isbn}</td>
                                <td>${item.genre}</td>
                                <td>${item.author}</td>
                                <td>${item.userNum}</td>
                            </tr>
                        `;
                        tableBody.append(tempHtml);
                    }
                }

                // 페이지네이션 생성
                generatePagination(data.totalPages);

                // 버튼 클릭 시 현재 페이지 엑셀 다운로드
                $('.revenue-current-page-excel').click(function () {
                    // 필요한 데이터만 골라서 추출
                    const refinedData = data.content.map(item => ({
                        storeName: item.storeName,
                        pointPayName: item.pointPayName,
                        pointPrice: item.pointPrice,
                        rentStart: item.rentStart,
                        rentEnd: item.rentEnd,
                        returnDate: item.returnDate,
                        overdueDays: item.overdueDays,
                        isbn: item.isbn,
                        genre: item.genre,
                        author: item.author,
                        userNum: item.userNum
                    }));

                    // 엑셀로 다운로드
                    if (refinedData.length > 0) {
                        window.downloadExcel(refinedData);
                    } else {
                        alert("다운로드할 데이터가 없습니다.");
                    }
                });


            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Ajax 요청 오류:", textStatus, errorThrown);
            }
        });
    }

    // 페이지네이션 버튼 생성 함수
    const generatePagination = function (totalPages) {
        let pagination = $('.pagination');
        pagination.empty();

        // 이전 페이지 버튼
        let prevDisabled = page === 0 ? 'disabled' : '';
        pagination.append(`
            <li class="page-item ${prevDisabled}">
                <a class="page-link" href="#" aria-label="Previous" data-page="${page - 1}">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
        `);

        // 페이지 번호 버튼
        for (let i = 0; i < totalPages; i++) {
            let active = page === i ? 'active' : '';
            pagination.append(`
                <li class="page-item ${active}">
                    <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
                </li>
            `);
        }

        // 다음 페이지 버튼
        let nextDisabled = page === totalPages - 1 ? 'disabled' : '';
        pagination.append(`
            <li class="page-item ${nextDisabled}">
                <a class="page-link" href="#" aria-label="Next" data-page="${page + 1}">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        `);

        // 페이지 클릭 이벤트 바인딩
        $('.page-link').on('click', function (e) {
            e.preventDefault();
            const selectedPage = $(this).data('page');
            if (selectedPage >= 0 && selectedPage < totalPages) {
                page = selectedPage;
                populateTable(); // 선택된 페이지의 데이터 로드
            }
        });
    }

    // 초기 테이블 데이터 로드
    populateTable();

    // 검색 버튼 클릭 이벤트
    $('#search-button').on('click', function () {
        startDate = $('#startDate').val();
        endDate = $('#endDate').val();
        pointOption = $('#salesItem').text().trim();
        storeName = $('#branch').text().trim();
        genre = $('#genre').text().trim();
        page = 0; // 검색 조건 변경 시 페이지 번호 초기화
        populateTable(); // 테이블 재로드
    });

    $('.revenue-current-all-excel').click(function () {
        $.ajax({
            url: '/admin/statistics/revenueAll',
            method: 'GET',
            data: {
                startDate: startDate,
                endDate: endDate,
                pointOption: pointOption,
                storeName: storeName,
                genre: genre
            },
            success: function (data) {
                // 필요한 데이터만 골라서 추출
                const refinedData = data.map(item => ({
                    storeName: item.storeName,
                    pointPayName: item.pointPayName,
                    pointPrice: item.pointPrice,
                    rentStart: item.rentStart,
                    rentEnd: item.rentEnd,
                    returnDate: item.returnDate,
                    overdueDays: item.overdueDays,
                    isbn: item.isbn,
                    genre: item.genre,
                    author: item.author,
                    userNum: item.userNum
                }));

                // 엑셀로 다운로드
                if (refinedData.length > 0) {
                    window.downloadExcel(refinedData);
                } else {
                    alert("다운로드할 데이터가 없습니다.");
                }

            }

        });
    })



});
