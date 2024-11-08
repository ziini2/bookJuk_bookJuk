async function searchBooks(event) {
    event.preventDefault();
    
    const criteria = document.getElementById("searchCriteria").value;
    const keyword = document.getElementById("searchKeyword").value;
    
    const response = await fetch(`/admin/searchbook?criteria=${criteria}&keyword=${encodeURIComponent(keyword)}`);
    const books = await response.json();
    
    const resultsTableBody = document.getElementById("resultsTableBody");
    resultsTableBody.innerHTML = ""; // 이전 결과 지우기
    
    books.forEach(book => {
        const row = document.createElement("tr");

        // 행을 클릭하면 rent.html의 모달 창에 데이터 전달
        row.addEventListener("click", () => populateModal(book));

        const bookNumCell = document.createElement("td");
        bookNumCell.textContent = book.bookNum;
        row.appendChild(bookNumCell);
        
        const bookNameCell = document.createElement("td");
        bookNameCell.textContent = book.bookName;
        row.appendChild(bookNameCell);
        
        const storeNameCell = document.createElement("td");
        storeNameCell.textContent = book.storeName;
        row.appendChild(storeNameCell);
        
        resultsTableBody.appendChild(row);
    });
}

function populateModal(book) {
    // rent.html의 모달 창 필드에 선택한 책 정보를 설정합니다.
    window.opener.document.getElementById("bookNum").value = book.bookNum;
    window.opener.document.getElementById("bookName").value = book.bookName;
    window.opener.document.getElementById("storeName").value = book.storeName;

    // 팝업 창을 닫습니다.
    window.close();
}
