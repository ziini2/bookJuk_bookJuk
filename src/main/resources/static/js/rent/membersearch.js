// membersearch.js
function searchMembers(event) {
    event.preventDefault(); // 기본 폼 제출 방지
    const criteria = document.getElementById("searchCriteria").value;
    const keyword = document.getElementById("searchKeyword").value;

    // 서버에 검색 요청 전송
    fetch(`/searchMembers?criteria=${criteria}&keyword=${keyword}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('검색 결과:', data); // 결과 콘솔 출력으로 확인
            displaySearchResults(data); // 검색 결과 표시 함수 호출
        })
        .catch(error => {
            console.error('검색 요청 중 오류 발생:', error);
        });
}

function displaySearchResults(data) {
    const resultsDiv = document.getElementById("searchResults");
    resultsDiv.innerHTML = ""; // 기존 결과 초기화

    if (data.length > 0) {
        const list = document.createElement("ul");
        list.classList.add("list-group");

        data.forEach(member => {
            const listItem = document.createElement("li");
            listItem.classList.add("list-group-item");
            listItem.textContent = `이름: ${member.userName}, 아이디: ${member.userId}, 연락처: ${member.userPhone}`;
            list.appendChild(listItem);
        });

        resultsDiv.appendChild(list);
    } else {
        resultsDiv.innerHTML = "<p>검색 결과가 없습니다.</p>";
    }
}