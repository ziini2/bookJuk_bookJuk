let code; //서버로 부터 받은 인증 코드

//SMS 보내기 버튼 클릭시
validateSMS = () => {
  if ($("#input-code").length === 0) {
    $("#input-validateNumber").append(
      `<i class="bx bx-phone"></i>
        <input id="input-code" type="text" placeholder="인증번호" />
        <div class="phoneValidate">
          <button id="sms-validate" onclick=checkSmsCode() type="submit">인증하기</button>
        </div>
      </div>`
    )
  }

  const userPhone = $("#join-userPhone");

  $.ajax({
    type: "POST",
    url: "/sendSmsCodeTest",
    contentType: "application/json",
    data: JSON.stringify({
      userPhone: userPhone.val(),
    }),
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

//SMS 인증 버튼 클릭시
checkSmsCode = () => {
  const input_code = $("#input-code");
  //유저가 입력한 코드의 번호를 저장하기 위한 변수
  let userCode = input_code.val().trim();

  if (userCode === String(code)){
    //에러메세지가 있는경우 에러메세지 삭제
    $(".error-message").remove();
    //성공시 입력했던 코드 비활성화 및 테두리 색상 변경
    input_code.css("border-color", "#4ea685");
    input_code.prop("disabled", true);
    //인증하기 버튼 비활성화
    $("#validate-button").prop("disabled", true);
    //유저 폰번호 입력란 비활성화
    $("#join-userPhone").prop("disabled", true);
    //sms 메세지 보내기 버튼 비활성화
    $("#sms-validate").prop("disabled", true);
    //마지막 저장하기버튼 활성화
    $("#saveButton").prop("disabled", false);
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


