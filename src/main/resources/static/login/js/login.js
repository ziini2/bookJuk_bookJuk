let container = document.getElementById("container");

toggle = () => {
  container.classList.toggle("sign-in");
  container.classList.toggle("sign-up");
};

setTimeout(() => {
  container.classList.add("sign-in");
}, 200);


$(document).ready(function () {
  //로그인 버튼 클릭시
  $("#loginButton").on('click', (e) => {
    e.preventDefault();

    const userId = $('#userId').val();
    const userPassword = $('#userPassword').val();

    $.ajax({
      type: "POST",
      url: "/loginCheck",
      data: {
        userId: userId,
        userPassword: userPassword
      },
      success: function (response) {
        console.log("로그인 성공" );
        window.location.href = "/";
      },
      error: function (xhr, status, error) {
        console.log("로그인 실패");

        //로그인 실패시 로직
        window.location.href = "/login";
      }
    });
  });

  //회원가입 버튼 클릭시
  $("#joinButton").on('click', (e) => {
    e.preventDefault();
    console.log("머튼 클릭")

    //폼데이터 직렬화
    const joinFormData = new FormData($("#joinForm")[0]);
    console.log(joinFormData);

    $.ajax({
      type: "POST",
      url: "/join",
      data: joinFormData,
      processData: false, // jQuery가 데이터를 처리하지 않도록 설정
      contentType: false, // 콘텐츠 타입을 자동 설정하지 않도록 설정
      success: function (response) {
        console.log("회원가입 성공");
        window.location.href = "/login";
      },
      error: function (xhr, status, error) {
        console.log("회원가입 실패" + error);
      }
    });
  });
});