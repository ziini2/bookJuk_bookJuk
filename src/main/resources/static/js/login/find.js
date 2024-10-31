$(document).ready(function () {

  //아이디 찾기 버튼 클릭시
  $("#findId").on("click",(e) => {
    e.preventDefault();

    const userName = $("#findId-userName");
    const userEmail = $("#findId-userEmail");

    $.ajax({
      type: "POST",
      url: "/login/findId",
      contentType: "application/json",
      data: JSON.stringify({
        userName: userName.val(),
        userEmail: userEmail.val()
      }),
      success: (res) => {
        if (res.RESULT === "SUCCESS") {
          console.log("아이디 찾기 성공")
          console.log(res.userId);
          const result = $(`
          <i class="bx bxs-lock-alt"></i>
          <input id="findId-userId" type="text" readonly/>
        `)
          if ($("#findId-userId").length === 0) {
            $("#findId-result").append(result);
            $("#findId-userId").val(res.userId);
          }
          $("#findId-userId").css("border-color", "#4ea685");
          userName.prop("disabled", true);
          userEmail.prop("disabled", true);
        }
        else {
          alert("존재하지 않는 회원입니다.")
          console.log("존재하지 않는 회원입니다.")
        }
      },
      error: (res) => {
        console.log(res);
      }
    })
  })

  //우선 아이디 랑 휴대폰 번호의 값을 한번 디비에서 조회해야함!
  //디비에 아이디 와 전화번호가 일치한다면 인증번호 보내기 버튼 활성화 하고
  //그런다음 sms 인증 하고
  $("#join-userPhone").blur( () => {
    const userId = $("#findPassword-userId");
    const userPhone = $("#join-userPhone");

    //입력필드 값이 비어있는지 확인
    if (!userId.val().trim()){
      alert("아이디를 입력해주세요.")
      userId.focus()
      return;
    }
    if (!userPhone.val().trim()){
      alert("전화번호를 입력해주세요.")
      return;
    }

    $.ajax({
      type: "POST",
      url: "/login/findCheck",
      contentType: "application/json",
      data: JSON.stringify({
        userId: userId.val(),
        userPhone: userPhone.val(),
      }),
      success: (res) => {
        if (res.RESULT === "SUCCESS") {
          //보내기 버튼 활성화
          if (!$("#join-userPhone").prop("readonly")){
            $("#validate-button").prop("disabled", false);
          }
          else {
            $("#validate-button").prop("disabled", true);
          }
          userId.prop("readonly", true);
        }
        else {
          //보내기 버튼 비활성화
          console.log("아이디 / 전화번호 불일치")
          alert("아이디 또는 전화번호가 일치하지 않습니다.")
        }
      },
      error: (res) => {
        console.log(res);
      }
    });
  })

  //비밀번호 찾기 버튼 클릭시
  //인증 완료된 상태면 비밀번호 찾기 버튼 클릭시 임시비밀번호 발급
  $("#findPassword").on('click', (e) => {
    $("#findPassword").hide();
    $("#loading").show();
    e.preventDefault();
    const userId = $("#findPassword-userId");
    const userPhone = $("#join-userPhone");

    $.ajax({
      type: "POST",
      url: "/login/findPass",
      contentType: "application/json",
      data: JSON.stringify({
        userId: userId.val(),
        userPhone: userPhone.val()
      }),
      success: (res) => {
        if(res.RESULT === "SUCCESS"){
          $("#loading").hide();
          alert("가입한 이메일로 임시비밀번호 전송하였습니다.\n 로그인후 비밀번호를 변경해주세요!")
          location.href="/login"
        }
        else {
          alert("오류입니다.")
        }
      },
      error: (res) => {
        console.log(res);
      }
    })
  })

  //전화번호 '-' 자동으로 생성
  $('#join-userPhone').on('input', function (e) {
    //숫자만 남기기
    let value = $(this).val().replace(/[^0-9]/g, '');

    // 백스페이스 시 포맷 처리 유연하게 유지
    if (e.originalEvent && e.originalEvent.inputType === 'deleteContentBackward') {
      return; // 기본 삭제 동작 유지
    }

    //3자리 후 '-' 추가
    if (value.length >= 3) {
      value = value.slice(0, 3) + '-' + value.slice(3);
    }
    //4자리 후 '-' 추가
    if (value.length >= 8) {
      value = value.slice(0, 8) + '-' + value.slice(8);
    }
    //최대 길이 제한
    $(this).val(value.slice(0, 13));
  });

})