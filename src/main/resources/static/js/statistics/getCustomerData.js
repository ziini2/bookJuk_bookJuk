$(document).ready(function () {

    let startDate = '1000-01-01';
    let endDate = '1000-01-01';
    let gender = $('#gender').text().trim();
    let age = $('#age').text().trim();
    let page = 0;
    let size = 20;

    // 버튼 클릭 시 전체 페이지 엑셀 다운로드
    $('.customer-current-all-excel').click(function () {
        downloadAllExcel();
    });


    const populateTable = function () {
        $.ajax({
            url: '/admin/statistics/customer',
            method: 'GET',
            data: {
                startDate: startDate,
                endDate: endDate,
                gender: gender,
                age: age,
                page: page,
                size: size
            },
            success: function (data) {
                let tableBody = $('tbody');
                tableBody.empty();

                if (data.content.length === 0) {
                    tableBody.append('<tr><td colspan="12" class="text-center">조회된 데이터가 없습니다.</td></tr>');
                } else {
                    for (let i = 0; i < data.content.length; i++) {
                        const item = data.content[i];
                        const tempHtml = `
                            <tr>
                                <td>${data.totalElements - (page * size) - i}</td>
                                <td>${item.userNum}</td>
                                <td>${item.joinDate}</td>
                                <td>${item.totalRentPrice}</td>
                                <td>${item.totalOverduePrice}</td>
                                <td>${item.totalPaymentPrice}</td>
                                <td>${item.totalCouponPrice}</td>
                                <td>${item.gender}</td>
                                <td>${item.age}</td>
                                <td>${item.totalRentDays}</td>
                                <td>${item.totalRentCount}</td>
                                <td>${item.totalOverdueDays}</td>
                                <td>${item.totalOverdueCount}</td>
                            </tr>
                        `;
                        tableBody.append(tempHtml);
                    }
                }
                // 페이지네이션 생성
                generatePagination(data.totalPages);

                // 버튼 클릭 시 현재 페이지 엑셀 다운로드
                downloadExcel('.customer-current-page-excel', data);


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
        $('.page-link').off('click').on('click', function (e) {
            e.preventDefault();
            const selectedPage = $(this).data('page');
            if (selectedPage >= 0 && selectedPage < totalPages) {
                page = selectedPage;
                populateTable(); // 선택된 페이지의 데이터 로드
            }
        });
    }


    populateTable();


    // 검색 버튼 클릭 이벤트
    $('#search-button').on('click', function () {
        startDate = $('#startDate').val();
        endDate = $('#endDate').val();
        gender = $('#dropdownMenuButton3').text().trim(); // 드롭다운에서 선택된 성별 값 반영
        age = $('#dropdownMenuButton4').text().trim(); // 드롭다운에서 선택된 나이 값 반영
        page = 0; // 검색 조건 변경 시 페이지 번호 초기화
        populateTable(); // 테이블 재로드
    });

    function downloadExcel(tag, data) {
        $(tag).click(function () {
            // 필요한 데이터만 골라서 추출
            const refinedData = data.content.map(item => ({
                userNum: item.userNum,
                joinDate: item.joinDate,
                totalRentPrice: item.totalRentPrice,
                totalOverduePrice: item.totalOverduePrice,
                totalPaymentPrice: item.totalPaymentPrice,
                totalCouponPrice: item.totalCouponPrice,
                gender: item.gender,
                age: item.age,
                totalRentDays: item.totalRentDays,
                totalRentCount: item.totalRentCount,
                totalOverdueDays: item.totalOverdueDays,
                totalOverdueCount: item.totalOverdueCount
            }));

            // 엑셀로 다운로드
            if (refinedData.length > 0) {
                window.downloadExcel(refinedData);
            } else {
                alert("다운로드할 데이터가 없습니다.");
            }
        });
    }

    function downloadAllExcel() {
            $.ajax({
                url: '/admin/statistics/customerAll',
                method: 'GET',
                data: {
                    startDate: startDate,
                    endDate: endDate,
                    startDate: startDate,
                    endDate: endDate,
                    gender: gender,
                    age: age
                },
                success: function (data) {
                    const refinedData = data.map(item => ({
                        userNum: item.userNum,
                        joinDate: item.joinDate,
                        totalRentPrice: item.totalRentPrice,
                        totalOverduePrice: item.totalOverduePrice,
                        totalPaymentPrice: item.totalPaymentPrice,
                        totalCouponPrice: item.totalCouponPrice,
                        gender: item.gender,
                        age: item.age,
                        totalRentDays: item.totalRentDays,
                        totalRentCount: item.totalRentCount,
                        totalOverdueDays: item.totalOverdueDays,
                        totalOverdueCount: item.totalOverdueCount
                    }));

                    // 엑셀로 다운로드
                    if (refinedData.length > 0) {
                        window.downloadExcel(refinedData);
                    } else {
                        alert("다운로드할 데이터가 없습니다.");
                    }
                }
            })
        }
});
