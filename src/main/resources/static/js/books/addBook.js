let isBookDataValid = false; // ISBN 검색 성공 여부를 저장하는 변수

// ISBN을 조회하여 도서 정보를 자동으로 채우는 함수
async function isbnSearch() {
    const isbn = document.getElementById("isbn").value;
    const url = `https://www.nl.go.kr/seoji/SearchApi.do?cert_key=6e3c1f31ab8b477b3346a6d485a280dcebfbdd9f97fe34c373133551f4fc8616&result_style=json&page_no=1&page_size=1&isbn=${isbn}`;

    try {
        const response = await fetch(url);
        const data = await response.json();

        if (data.docs && data.docs.length > 0) {
            const book = data.docs[0];
            document.getElementById("bookName").value = book.TITLE || "";
            document.getElementById("author").value = book.AUTHOR || "";
            document.getElementById("publish").value = book.PUBLISHER || "";
            document.getElementById("publishDate").value = book.PUBLISH_PREDATE || "";

            // 줄거리 대신 책 소개나 다른 필드 사용
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

// 도서 등록 함수 (서버로 데이터를 전송)
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
    const data = {
        isbn: formData.get("isbn"),
        bookName: formData.get("bookName"),
        author: formData.get("author"),
        publish: formData.get("publish"),
        publishDate: formData.get("publishDate"),
        story: formData.get("story"),
    };
	console.log(JSON.stringify(data));

    try {
        const response = await fetch("/admin/addBook", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data),
        });

        if (response.ok) {
            alert("도서가 등록되었습니다.");
            const modal = bootstrap.Modal.getInstance(document.getElementById('exampleModal'));
            modal.hide();
            form.reset();
            isBookDataValid = false; // 등록 후 다시 초기화
        } else {
            alert("도서 등록에 실패했습니다. 다시 시도해주세요.");
        }
    } catch (error) {
        console.error("서버로 데이터 전송 중 오류 발생:", error);
        alert("서버와의 통신에 문제가 발생했습니다.");
    }
}
