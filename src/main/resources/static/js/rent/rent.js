//	멤버 검색 팝업
          function openMemberSearchPopup() {
              window.open(
                  'membersearch',  // 팝업에 표시할 페이지를 별도로 생성
                  '회원 검색',            // 팝업 창 이름
                  'width=600,height=400,scrollbars=yes' // 창 크기와 옵션 설정
              );
          }

		  
	  
//	책 검색 팝업

		function openBookSearchPopup() {
			window.open(
				'booksearch',  // 팝업에 표시할 페이지를 별도로 생성
				'책 검색',            // 팝업 창 이름
				'width=600,height=400,scrollbars=yes' // 창 크기와 옵션 설정
			);
		}

		// 현재 날짜 가져오기 및 초기화 버튼 클릭 시 현재 날짜 설정
		const today = new Date().toISOString().split('T')[0];
		const rentalDateInput = document.getElementById('rentalDate');
		rentalDateInput.value = today;

		// 초기화 버튼 눌렀을 때, 대여일 다시 현재 날짜로 설정
		document.getElementById('rentalForm').addEventListener('reset', function() {
			setTimeout(function() {
			rentalDateInput.value = today;
		}, 0); // 초기화 후 바로 오늘 날짜로 세팅
	});

	
	
//	드랍박스 선택시 자동 db적용

		function updateReturnInfo(selectElement, rentNum) {
		    const returnInfo = selectElement.value;

		    fetch(`/updateReturnInfo`, {
		        method: 'POST',
		        headers: {
		            'Content-Type': 'application/json',
		        },
		        body: JSON.stringify({
		            rentNum: rentNum,
		            returnInfo: returnInfo
		        }),
		    })
		    .then(response => {
		        if (response.ok) {
		            alert("반납 상태가 성공적으로 업데이트되었습니다.");
		            location.reload(); // 페이지 새로고침
		        } else {
		            alert("업데이트 중 오류가 발생했습니다.");
		            location.reload(); // 오류 발생 시에도 페이지 새로고침
		        }
		    })
		    .catch(error => {
		        console.error("에러:", error);
		        location.reload(); // 네트워크 오류 발생 시에도 새로고침
		    });
		}



//	검색
// 검색
document.addEventListener("DOMContentLoaded", function() {
    function performSearch(page = 1) {
        const criteria = document.getElementById("criteria").value;
        const keyword = document.getElementById("keyword").value;
        const size = 10; // 페이지당 결과 개수
	
		// 서버에서 렌더링된 페이징 부분 제거
		       const serverPagination = document.getElementById("serverPagination");
		       if (serverPagination) {
		           serverPagination.remove(); // DOM에서 완전히 제거
		       }
		
        fetch(`/rent/search?criteria=${criteria}&keyword=${keyword}&page=${page}&size=${size}`)
            .then(response => response.json())
            .then(data => {
                const tableBody = document.getElementById("rentalTableBody");
                const paginationContainer = document.getElementById("pagination");

                if (!tableBody) {
                    console.error("tableBody 요소를 찾을 수 없습니다.");
                    return;
                }

                tableBody.innerHTML = ""; // 기존 데이터를 지움
                paginationContainer.innerHTML = ""; // 기존 페이징 버튼 초기화

                data.content.forEach(rent => {
                    const row = document.createElement("tr");

                    const returnDate = rent.returnDate ? 
                        new Date(rent.returnDate).toISOString().split('T')[0] : "";

                    row.innerHTML = `
                        <td>${rent.rentNum}</td>
                        <td>${rent.userNum}</td>
                        <td>${rent.userId}</td>
                        <td>${rent.userName}</td>
                        <td>${rent.userPhone}</td>
                        <td>${rent.bookNum}</td>
                        <td>${rent.bookName}</td>
                        <td>${new Date(rent.rentDate).toISOString().split('T')[0]}</td>
                        <td>${returnDate}</td>
                        <td>
                            <select onchange="updateReturnInfo(this, ${rent.rentNum})">
                                <option value="대여중" ${rent.returnInfo === '대여중' ? 'selected' : ''}>대여중</option>
                                <option value="연체중" ${rent.returnInfo === '연체중' ? 'selected' : ''}>연체중</option>
                                <option value="반납완료" ${rent.returnInfo === '반납완료' ? 'selected' : ''}>반납완료</option>
                            </select>
                        </td>
                    `;
                    tableBody.appendChild(row);
                });

                // 페이지 네비게이션 업데이트
                if (data.totalPages > 1) { // 총 페이지가 1 이상일 때만 페이징 버튼 생성
                    updatePagination(data.totalPages, page);
                }
            })
            .catch(error => console.error("검색 중 오류 발생:", error));
    }

    function updatePagination(totalPages, currentPage) {
        const paginationContainer = document.getElementById("pagination");
        paginationContainer.innerHTML = ""; // 기존 버튼 초기화

        for (let page = 1; page <= totalPages; page++) {
            const pageButton = document.createElement("button");
            pageButton.textContent = page;
            pageButton.classList.add("btn", "btn-secondary", "mx-1");
            if (page === currentPage) {
                pageButton.classList.add("active");
            }
            pageButton.onclick = () => performSearch(page);
            paginationContainer.appendChild(pageButton);
        }
    }

    // 검색 버튼 클릭 시 performSearch 실행
    document.getElementById("searchButton").addEventListener("click", function() {
        performSearch(1); // 첫 페이지부터 검색
    });

    // 엔터키 입력 시 검색 실행
    document.getElementById("keyword").addEventListener("keydown", function(event) {
        if (event.key === "Enter") {
            event.preventDefault(); // 기본 엔터키 동작 방지 (폼 제출 방지)
            performSearch(1); // 첫 페이지부터 검색
        }
    });

    // 폼 제출 방지
    document.getElementById("searchForm").addEventListener("submit", function(event) {
        event.preventDefault(); // 기본 폼 제출 방지
        performSearch(1); // 첫 페이지부터 검색
    });
});




