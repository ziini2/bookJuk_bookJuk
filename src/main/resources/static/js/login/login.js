$(document).ready(function () {

  //로그인 버튼 클릭시
  $("#loginButton").on('click', (e) => {
    e.preventDefault();

    const userId = $('#userId').val();
    const userPassword = $('#userPassword').val();
    const autoLogin = $('#auto-login').val();
    console.log(userId + userPassword);

    $.ajax({
      type: "POST",
      url: "/loginCheck",
      data: {
        userId: userId,
        userPassword: userPassword,
        rememberMe: autoLogin
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

});