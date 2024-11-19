function rentBooks(){
  console.log("Ajax 호출 시작"); // Ajax 호출 전 디버깅
  $.ajax({
    type: "POST",
    url: "/user/rentDetail",
    success: (res) => {
      if (res.response === "success") {
        alert("대여 성공");
        location.href="/";
      } else {
        alert("대여 실패");
      }
    },
    error: (err) => {
      console.error("Ajax 요청 오류:", err);
    }
  });
}
