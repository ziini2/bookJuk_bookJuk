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
        
        const nameCell = document.createElement("td");
        nameCell.textContent = user.userName;
        row.appendChild(nameCell);
        
        const idCell = document.createElement("td");
        idCell.textContent = user.userId;
        row.appendChild(idCell);
        
        const phoneCell = document.createElement("td");
        phoneCell.textContent = user.userPhone; // userPhone 필드 사용
        row.appendChild(phoneCell);
        
        resultsTableBody.appendChild(row);
    });
}
