async function searchMembers(event) {
    event.preventDefault();
    
    const criteria = document.getElementById("searchCriteria").value;
    const keyword = document.getElementById("searchKeyword").value;
    
    const response = await fetch(`/admin/search?criteria=${criteria}&keyword=${encodeURIComponent(keyword)}`);
    const users = await response.json();
    
    const resultsTableBody = document.getElementById("resultsTableBody");
    resultsTableBody.innerHTML = ""; // 이전 결과 지우기
    
    users.forEach(user => {
        const row = document.createElement("tr");

        row.addEventListener("click", () => populateModal(user)); // 행 클릭 시 이벤트 추가

        const nameCell = document.createElement("td");
        nameCell.textContent = user.userName;
        row.appendChild(nameCell);
        
        const idCell = document.createElement("td");
        idCell.textContent = user.userId;
        row.appendChild(idCell);
        
        const phoneCell = document.createElement("td");
        phoneCell.textContent = user.userPhone;
        row.appendChild(phoneCell);
        
        resultsTableBody.appendChild(row);
    });
}

function populateModal(user) {
    // 모달 창의 입력 필드 ID에 맞게 값을 설정합니다.
    opener.document.getElementById("userId").value = user.userId;
    opener.document.getElementById("userName").value = user.userName;
    opener.document.getElementById("userPhone").value = user.userPhone;

    // 회원 선택 후 팝업 창을 닫습니다.
    window.close();
}

