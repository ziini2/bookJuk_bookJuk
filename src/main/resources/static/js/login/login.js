$(document).ready(function () {

  //로그인 버튼 클릭시
  $("#loginButton").on('click', (e) => {
    e.preventDefault();

    const userId = $('#userId').val();
    const userPassword = $('#userPassword').val();
    const autoLogin = $('#auto-login').is(':checked')

    $.ajax({
      type: "POST",
      url: "/login/loginCheck",
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
        $("#userId").css("border-color", "red");
        $("#userPassword").css("border-color", "red");
        if ($(".error-message").length === 0) {
          // 동적으로 에러 메시지 <div> 생성 및 입력 그룹 밖에 추가
          const errorDiv = $('<div class="error-message">아이디 비밀번호를 확인해 주세요.</div>');
          // input-group 바로 아래에 추가
          $("#login-error-message").append(errorDiv);
        }
      }
    });
  });

});