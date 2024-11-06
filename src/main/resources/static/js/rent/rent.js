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
	document.addEventListener("DOMContentLoaded", function() {
		    function performSearch() {
		        const criteria = document.getElementById("criteria").value;
		        const keyword = document.getElementById("keyword").value;

		        fetch(`/rent/search?criteria=${criteria}&keyword=${keyword}`)
		            .then(response => response.json())
		            .then(data => {
		                const tableBody = document.getElementById("rentalTableBody");

		                // rentalTableBody 요소가 있는지 확인
		                if (!tableBody) {
		                    console.error("tableBody 요소를 찾을 수 없습니다.");
		                    return;
		                }

		                tableBody.innerHTML = ""; // 기존 데이터를 지움

		                data.forEach(rent => {
		                    const row = document.createElement("tr");

		                    // 반납일이 존재하는 경우에만 표시
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
		            })
		            .catch(error => console.error("검색 중 오류 발생:", error));
		    }

		    // 검색 버튼 클릭 시 performSearch 실행
		    document.getElementById("searchButton").addEventListener("click", performSearch);

		    // 엔터키 입력 시 기본 폼 제출 방지하고 performSearch 실행
		    document.getElementById("searchForm").addEventListener("submit", function(event) {
		        event.preventDefault(); // 기본 폼 제출 방지
		        performSearch();
		    });
		});



