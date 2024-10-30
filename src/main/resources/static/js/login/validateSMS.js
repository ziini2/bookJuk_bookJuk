//유효시간 변수화
let timer;

//SMS 보내기 버튼 클릭시
validateSMS = () => {
  if ($("#input-code").length === 0) {
    $("#input-validateNumber").append(
      `<i class="bx bx-phone"></i>
        <input id="input-code" type="text" placeholder="인증번호" />
        <div class="phoneValidate" id="deleteCode">
          <button id="sms-validate" onclick=checkSmsCode() type="button">인증하기</button>
        </div>`
    )
  }
  if ($("#timer").length === 0) {
    $(".timer").append(
      `<p id="timer">남은 시간: 00:30</p>`
    )
    timerLeft = 30;
    timer = setInterval(updateTimer, 1000);
  }

  const userPhone = $("#join-userPhone");
  console.log(userPhone.val());

  $.ajax({
    type: "POST",
    url: "/login/sendSmsCodeTest",
    contentType: "application/json",
    data: userPhone.val(),
    success: (res) => {
      if(res.RESULT === "SUCCESS"){
        code = res.code;
        console.log("SMS 인증 코드 전송")
        alert("인증번호가 발송되었습니다.")
        console.log(code)
      }
      else {
        console.log("SMS 인증 실패")
        alert("인증번호가 발송실패 되었습니다.")
      }
    },
    error: (err) => {
      alert("오류: " + err.responseText);
      console.error(err);
    }
  })
};

//SMS 인증 검증 API
checkSmsCode = () => {
  const input_code = $("#input-code");
  //유저가 입력한 코드의 번호를 저장하기 위한 변수
  let userCode = input_code.val().trim();
  $.ajax({
    type: "POST",
    url: "/login/codeValidate",
    contentType: "application/json",
    data: userCode,
    success: (res) => {
      if (res.RESULT === "SUCCESS"){
        alert("인증성공")
        //에러메세지가 있는경우 에러메세지 삭제
        $(".error-message").remove();
        //인증 성공시 타이머 종료 및 타이머 삭제
        clearInterval(timer);
        $("#timer").remove();
        //성공시 입력했던 코드 비활성화 및 테두리 색상 변경
        input_code.css("border-color", "#4ea685");
        input_code.prop("disabled", true);
        //SMS 보내기 버튼 비활성화
        $("#validate-button").prop("disabled", true);
        //유저 폰번호 입력란 비활성화
        $("#join-userPhone").prop("readonly", true);
        //인증하기 버튼 비활성화
        $("#sms-validate").prop("disabled", true);
        //마지막 저장하기버튼 활성화
        $("#saveButton").prop("disabled", false);
        //비밀번호 찾기 페이지 버튼 활성화
        $("#findPassword").prop("disabled", false);
        //비밀번호 찾기 페이지 조회 방지로 (blur)이벤트 해제
        $("#join-userPhone").off("blur");
      }
      else {
        //에러메세지가 존재 하는지 아닌지 판단하기
        if ($(".error-message").length === 0){
          //인증코드 입력란 테두리 색상변경
          input_code.css("border-color", "red");
          //에러 메세지 출력
          const errorDiv = $('<div class="error-message" style="color:#db4437"> 인증번호가 틀립니다! </div>');
          $("#error-message").append(errorDiv);
        }
      }
    }
  })

}

//소셜로그인 유저 전화번호 따로 저장하기 버튼 클릭시
saveUserPhone = () => {
  const userPhone = $("#join-userPhone").val()
  $.ajax({
    type: "POST",
    url: "/login/join/phone",
    contentType: "application/json",
    data: userPhone,
    success: (res) => {
      if (res.RESULT === "SUCCESS"){
        alert("저장되었습니다.")
        window.location.href = "/";
      }
      else{
        alert("오류입니다. 관리자에게 문의하여 주세요 email(bookjuk@bookjuk.com)");
        window.location.href = "/login";
      }
    }
  })
}

