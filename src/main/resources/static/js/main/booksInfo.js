function getBooksInfo(id) {

  const pk = parseInt(id.getAttribute("data-pk"));

  // 필요한 추가 작업 수행
  $.ajax({
    type: "POST",
    url: "/main/getBooksInfo",
    data: { booksId: pk },
    success: (res) => {
      if (res.response === "success") {
        console.log(res.result);
        // 데이터 처리 로직 추가
        const result = res.result.bookNum;
        if (result.bookImage === null) $("#bookInfoImage").prop("src", "/images/books/noimage.png");
        else $("#bookInfoImage").prop("src", result.bookImage);
        $("#bookName").text(result.bookName);
        $("#bookAuthor").text(result.author);
        $("#bookPublish").text(result.publish);
        $("#bookGenreName").text(result.genre.genreName);
        $("#bookPublishDate").text(result.publishDate);
        $("#bookStory").text(result.story);
        $("#bookRentMoney").text(Number(result.rentMoney).toLocaleString("ko-KR") + " 원");
        $("#bookStore").text(res.result.storeCode);

        //버튼에 pk 값 넣어놓고 장바구니 추가할때 그 pk 값 넣어주기
        $(".bookCartAdd").attr("data-pk", res.result.booksId);

        //모달오픈 (나중에 API 반환 성공시)
        $("#bookInfoModal").modal("show");
      } else {
        console.error("Failed to retrieve book info");
      }
    },
    error: (err) => {
      console.log("Error:", err);
    }
  })
}