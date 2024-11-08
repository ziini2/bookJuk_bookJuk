// 검색 버튼 클릭 이벤트 리스너
document.getElementById("searchButton").addEventListener("click", function () {
    const criteria = document.getElementById("criteria").value;
    const keyword = document.getElementById("keyword").value;

    if (keyword.trim() === "") {
        alert("검색어를 입력해주세요.");
        return;
    }

    // 검색 요청
    fetch(`/rent/search?criteria=${criteria}&keyword=${keyword}&page=1&size=10`)
        .then(response => response.json())
        .then(data => {
            displayRentalData(data.content); // 검색 결과를 화면에 표시
            updatePagination(data); // 페이징 업데이트
        })
        .catch(error => console.error("검색 중 오류 발생:", error));
});

// 테이블에 대여 데이터 표시
function displayRentalData(rentals) {
    const rentalTableBody = document.getElementById("rentalTableBody");
    rentalTableBody.innerHTML = ""; // 기존 데이터 초기화

    rentals.forEach(rent => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${rent.bookName}</td>
            <td>${formatDate(rent.rentDate)}</td>
            <td>${rent.overDate || ""}</td>
            <td>${formatDate(rent.returnDate)}</td>
            <td>${rent.returnInfo}</td>
        `;

        rentalTableBody.appendChild(row);
    });
}

// 날짜 형식 변환 함수
function formatDate(date) {
    if (!date) return "";
    const d = new Date(date);
    return d.toISOString().split("T")[0]; // "YYYY-MM-DD" 형식으로 변환
}

// 페이징 업데이트
function updatePagination(data) {
    const paginationContainer = document.getElementById("serverPagination");
    paginationContainer.innerHTML = "";

    if (data.totalPages <= 1) return; // 페이지가 1개 이하일 경우 페이징 숨김

    // 이전 버튼
    if (data.pageable.pageNumber > 0) {
        const prevButton = createPaginationButton(data.pageable.pageNumber, "이전");
        paginationContainer.appendChild(prevButton);
    }

    // 페이지 번호 버튼 생성
    for (let i = 0; i < data.totalPages; i++) {
        const pageButton = createPaginationButton(i + 1, i + 1);
        if (i === data.pageable.pageNumber) {
            pageButton.classList.add("active");
        }
        paginationContainer.appendChild(pageButton);
    }

    // 다음 버튼
    if (data.pageable.pageNumber < data.totalPages - 1) {
        const nextButton = createPaginationButton(data.pageable.pageNumber + 2, "다음");
        paginationContainer.appendChild(nextButton);
    }
}

// 페이징 버튼 생성 함수
function createPaginationButton(page, text) {
    const button = document.createElement("a");
    button.href = "#";
    button.classList.add("btn", "btn-secondary", "mx-1");
    button.textContent = text;

    button.addEventListener("click", function (e) {
        e.preventDefault();
        fetchAndDisplayRentData(page);
    });

    return button;
}

// 선택한 페이지 데이터 가져오기 및 표시
function fetchAndDisplayRentData(page) {
    const criteria = document.getElementById("criteria").value;
    const keyword = document.getElementById("keyword").value;

    fetch(`/rent/search?criteria=${criteria}&keyword=${keyword}&page=${page}&size=10`)
        .then(response => response.json())
        .then(data => {
            displayRentalData(data.content); // 검색 결과 표시
            updatePagination(data); // 페이징 업데이트
        })
        .catch(error => console.error("데이터 가져오기 오류:", error));
}

// 초기 데이터 로드
document.addEventListener("DOMContentLoaded", () => {
    fetchAndDisplayRentData(1);
});
