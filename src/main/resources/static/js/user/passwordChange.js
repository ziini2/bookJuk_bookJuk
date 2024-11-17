$(document).ready(function () {
  //모달이 완전히 닫힌 후 초기화
  $('#password-change').on('hidden.bs.modal', function () {
    resetModal('#password-change');
  });
  const userPass = $("#userPassword");
  //에이젝스 처리해야함..
  userPass.blur(() => userPasswordCheck());
  function userPasswordCheck() {
    userPass.closest('.input-group').next(".error-message").remove();
    $.ajax({
      type: "POST",
      url: "/user/passwordCheck",
      contentType: "text/plain",
      data:  userPass.val(),
      success: (res) => {
        if(res.response === "success"){
          console.log("성공");
          userPass.css("border-color", "#4ea685");
          $("#newPassword1").prop("disabled", false);
          $("#newPassword2").prop("disabled", false);
        }
        else {
          console.log("실패")
          userPass.css("border-color", "red");
          // 동적으로 에러 메시지 <div> 생성 및 입력 그룹 밖에 추가
          const errorDiv = $('<div class="error-message" style="color: red">일치하지 않습니다.</div>');
          // input-group 바로 아래에 추가
          userPass.closest('.input-group').after(errorDiv);
        }
      },
      error: (err) => {
        console.log(err.response)
      }
    });
  }


  //비밀번호 변경시
  //비밀번호1 유효성 검사
  const validatePassword = (password) => {
    //기존 비밀번호와 일치한지도 확인
    if (password === $("#userPassword").val()){
      return false;
    }
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;
    return passwordRegex.test(password);
  };

  //비밀번호2 유효성 검사
  const validatePassword2 = (password) => {
    const password1 = $("#newPassword1").val(); // 비밀번호1 값
    return password && password === password1 && password.length > 0;
  };

  //에러 메시지 업데이트 함수
  const updateErrorMessage = (inputId, isValid, errorMessage) => {
    const inputField = $(inputId);

    //기존 에러 메시지 제거
    inputField.closest(".input-group").next(".error-message").remove();

    if (!isValid) {
      //유효하지 않을 경우 에러 메시지 표시
      const errorDiv = $(`<div class="error-message"">${errorMessage}</div>`);
      inputField.closest(".input-group").after(errorDiv);
      inputField.css("border-color", "orange");
    } else {
      //유효한 경우 테두리 색상만 변경
      inputField.css("border-color", "#4ea685");
    }
  };

  //변경 버튼 활성화 처리 함수 (필드 전체 상태 검사)
  const updateChangeButtonState = () => {
    const password1 = $("#newPassword1").val();
    const password2 = $("#newPassword2").val();

    const isPassword1Valid = validatePassword(password1);
    const isPassword2Valid = validatePassword2(password2);

    //비밀번호1과 비밀번호2가 모두 유효하면 버튼 활성화
    if (isPassword1Valid && isPassword2Valid) {
      $("#passChangeButton").prop("disabled", false); // 버튼 활성화
    } else {
      $("#passChangeButton").prop("disabled", true); // 버튼 비활성화
    }
  };

  //비밀번호1 블러시 처리
  $("#newPassword1").blur(() => {
    const password1 = $("#newPassword1").val();
    //기존 비밀번호 동일한지 확인
    if (password1 === $("#userPassword").val()){
      updateErrorMessage("#newPassword1", false, "기존 비밀번호와 동일합니다.");
    }
    else {
      const isPassword1Valid = validatePassword(password1);
      updateErrorMessage("#newPassword1", isPassword1Valid, "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다.");
    }
    updateChangeButtonState(); // 버튼 활성화 상태 업데이트
  });

  //비밀번호2 블러시 처리
  $("#newPassword2").blur(() => {
    const password2 = $("#newPassword2").val();
    const isPassword2Valid = validatePassword2(password2);
    updateErrorMessage("#newPassword2", isPassword2Valid, "비밀번호가 일치하지 않습니다.");
    updateChangeButtonState(); // 버튼 활성화 상태 업데이트
  });

  //변경 버튼 클릭 이벤트
  $("#passChangeButton").on("click", (e) => {
    e.preventDefault();
    //새 비밀번호 값
    const newPassword = $("#newPassword1").val();

    $.ajax({
      type: "POST",
      url: "/user/changePassword",
      contentType: "text/plain",
      data: newPassword,
      success: (res) => {
        if (res.response === "success") {
          alert("비밀번호 변경 성공!");
          window.location.href = "/logout"; // 로그인 페이지로 이동
        } else {
          alert("비밀번호 변경 실패!");
        }
      },
      error: (err) => {
        console.error("비밀번호 변경 오류:", err);
        alert("비밀번호 변경 중 오류가 발생했습니다.");
      },
    });
  });


  //모달 리셋
  function resetModal(modalId) {
    const modal = $(modalId);

    //1.입력 필드 초기화
    modal.find("input").val("");

    //2.새 비밀번호 입력 비활성화
    $("#newPassword1, #newPassword2").prop("disabled", true);

    //3.에러 메시지 제거
    modal.find(".error-message").remove();

    //4.CSS 스타일 초기화
    modal.find("input").css({
      "border-color": "",
    });
    console.log("모달 내용 및 스타일 초기화 완료");
  }

})