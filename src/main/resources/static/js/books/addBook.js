let isBookDataValid = false;  // 초기값을 false로 설정

// ISBN 조회 함수
async function isbnSearch() {
    const isbn = document.getElementById("isbn").value;
	const url = `https://www.nl.go.kr/seoji/SearchApi.do?cert_key=6e3c1f31ab8b477b3346a6d485a280dcebfbdd9f97fe34c373133551f4fc8616&result_style=json&page_no=1&page_size=1&isbn=${isbn}`;

    if (!isbn || isbn.length !== 13) {
        alert("ISBN 번호는 13자리여야 합니다.");
        isBookDataValid = false;
        return;
    }

	try {
	    const response = await fetch(url);
	    const data = await response.json();

	    if (data.docs && data.docs.length > 0) {
	        const book = data.docs[0];
	        document.getElementById("bookName").value = book.TITLE;
	        document.getElementById("author").value = book.AUTHOR; 
	        document.getElementById("publish").value = book.PUBLISHER; 
	        document.getElementById("publishDate").value = book.PUBLISH_PREDATE; 
			document.getElementById("genreID").value = book.SUBJECT || ""; //주제정보
			document.getElementById("bookImage").src = book.TITLE_URL || "";

	        // 책소개
	        const bookDescription = book.DESCRIPTION || book.INTRO || "책 소개 없음";
	        document.getElementById("story").value = bookDescription;

	        isBookDataValid = true; // 도서 정보가 유효함을 표시
	    } else {
	        alert("도서를 찾을 수 없습니다.");
	        isBookDataValid = false; // 도서 정보가 유효하지 않음을 표시
	    }
	} catch (error) {
	    console.error("도서 조회 중 오류 발생:", error);
	    alert("도서 조회에 실패했습니다. 다시 시도해주세요.");
	    isBookDataValid = false;
	}
}

// 도서등록
async function registerBook() {
    const form = document.getElementById("addBook");

    if (!form.checkValidity()) {
        alert("모든 필드를 올바르게 입력해주세요.");
        form.reportValidity();
        return;
    }

    if (!isBookDataValid) {
        alert("ISBN 조회를 통해 유효한 도서 정보를 입력해주세요.");
        return;
    }

    const formData = new FormData(form);
    const publishDate = formData.get("publishDate");

    // "20201110" 형식의 날짜를 "2020-11-10" 형식으로 변환
    const formattedPublishDate = formatPublishDate(publishDate);
	const formattedBookDate = formatPublishDate(formData.get("bookDate"));
	
    const data = {
        isbn: formData.get("isbn"),
        bookName: formData.get("bookName"),
        author: formData.get("author"),
        publish: formData.get("publish"),
        publishDate: formattedPublishDate, // 변환된 날짜 사용
        story: formData.get("story"),
		storeCode: formData.get("storeCode"),
		bookStatus: formData.get("bookStatus"),
		bookDate : formattedBookDate,
		genreId : formData.get("genreId"),
		rentMoney : formData.get("rentMoney"),
		inventory : formData.get("inventory")
		
    };
    console.log(JSON.stringify(data));

    try {
        const response = await fetch("/admin/addBook", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data),  // 데이터를 body에 넣어 전송
        });

        const responseData = await response.json();  // JSON 응답 받기
        if (response.ok) {
            alert(responseData.message);  // 서버에서 전달한 메시지 표시
            const modal = bootstrap.Modal.getInstance(document.getElementById('exampleModal'));
            modal.hide();
            form.reset();
            isBookDataValid = false;  // 등록 후 다시 초기화
			alert("도서가 성공적으로 등록되었습니다!");
        } else {
            alert(responseData.message);  // 실패 메시지 표시
        }
    } catch (error) {
        console.error("서버로 데이터 전송 중 오류 발생:", error);
        alert("서버와의 통신에 문제가 발생했습니다.");
    }
}

// 날짜 포맷 변환 함수
function formatPublishDate(publishDate) {
    if (publishDate && publishDate.length === 8) {
        // "20201110"을 "2020-11-10"으로 변환
        return ${publishDate.slice(0, 4)}-${publishDate.slice(4, 6)}-${publishDate.slice(6, 8)};
    }
    return publishDate; // 잘못된 형식인 경우 그대로 반환
}
