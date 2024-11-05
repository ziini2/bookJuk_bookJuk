
//모든 에러 메시지 삭제 함수
clearAllErrorMessages = () => {
  $(".error-message").remove(); // 에러 메시지 클래스 전체 제거
  $(".input-group input").css("border-color", ""); // 입력 필드의 테두리 색 초기화
};

$(document).ready(function () {
  //#region 유효성 및 중복확인 함수 모음

  //유효성 검사 함수Å
  //아이디 유효성 검사
  const validateId = (userId) => {
    //5 ~ 15자 영문, 숫자 조합
    const userIdRegex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z0-9]{5,15}$/;
    return userIdRegex.test(userId);
  };
  //비밀번호1 유효성 검사
  const validatePassword = (password) => {
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;
    return passwordRegex.test(password);
  };
  //비밀번호2 유효성 검사
  const validatePassword2 = (password) => {
    const password1 = $("#join-userPassword1").val();
    if (password1 === password && password.length > 0) {
      return true;
    }
    return false;
  }
  //이름 유효성 검사
  const validateName = (name) => {
    const nameRegex = /^[가-힣]{2,}$/;
    return nameRegex.test(name);
  }
  //생년월일 유효성 검사
  const validateBirthday = (birthday) => {
    const birthdayRegex = /^(19|20)\d{2}년(0[1-9]|1[0-2])월(0[1-9]|[12]\d|3[01])일$/;
    return birthdayRegex.test(birthday);
  }
  //이메일 유효성 검사
  const validateEmail = (email) => {
    const emailRegex =  /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    //test() 메서드는 입력된 문자열이 정규식 패턴에 일치하는지 여부 판단후 true/false 반환
    return emailRegex.test(email);
  };
  //전화번호 유효성 검사
  const validatePhone = (phone) => {
    const phoneRegex = /^\d{3}-\d{4}-\d{4}$/;
    return phoneRegex.test(phone);
  };

  //유효성 검사 처리 함수
  const handleValidation = (formId, validator) => {
    const value = $(formId).val();

    // 기존의 에러 메시지 제거 (중복 방지)
    $(formId).closest('.input-group').next(".error-message").remove();

    if (validator(value)) {
      $(formId).css("border-color", "#4ea685");
      return true;
    }
    else {
      $(formId).css("border-color", "orange");
      // 동적으로 에러 메시지 <div> 생성 및 입력 그룹 밖에 추가
      const errorDiv = $('<div class="error-message">형식에 맞지 않습니다.</div>');
      // input-group 바로 아래에 추가
      $(formId).closest('.input-group').after(errorDiv);
      return false;
    }
  };
  //유효성 검사 + 중복확인 처리 함수
  const checkDuplicate = (formId, dataKey, validator) => {
    const value = $(formId).val();
    const validate_button = $("#validate-button");

    //유효성 검사 실패 시 중복 확인 하지 않음
    if(!handleValidation(formId, validator)){
      return;
    }

    $.ajax({
      type: "POST",
      url: "/login/checkData",
      contentType: "application/json",
      data: JSON.stringify({
        [dataKey]: value
      }),
      success: (res) => {
        if (!duplicateCheckResults[formId]){
          $(formId).closest('.input-group').next(".error-message").remove();
        }
        if(res.RESULT === "SUCCESS"){
          console.log("중복확인 성공")
          //성공 시 true 로 변경
          duplicateCheckResults[formId] = true;
          $(formId).css("border-color", "#4ea685");
          if (formId === "#join-userPhone"){
            if (!$("#join-userPhone").prop("readonly")){
              validate_button.prop("disabled", false);
            }
            else {
              validate_button.prop("disabled", true);
            }
          }
        }
        else {
          console.log("중복확인 실패")
          validate_button.text("보내기");
          $(formId).css("border-color", "red");
          //실패 시 false 로 변경
          duplicateCheckResults[formId] = false;
          // 동적으로 에러 메시지 <div> 생성 및 입력 그룹 밖에 추가
          const errorDiv = $('<div class="error-message" style="color:#db4437"> 중복됩니다.</div>');
          // input-group 바로 아래에 추가
          $(formId).closest('.input-group').after(errorDiv);
          $("#validate-button").prop("disabled", true);
          $("#sms-validate").remove();
        }
      },
      error: (err) => {
        alert("오류: " + err.responseText);
        console.error(err);
      }
    })
  }

  //중복체크 확인을 위한 전역설정
  const duplicateCheckResults = {
    "#join-userId": false,      //아이디 중복 체크
    "#join-userEmail": false,    //이메일 중복 체크
    "#join-userPhone": false     //전화번호 중복 체크
  }
  //중복체크
  //유효성 + 중복
  $("#join-userId").blur(() => checkDuplicate("#join-userId", "userId", validateId));
  $("#join-userEmail").blur(() => checkDuplicate("#join-userEmail", "userEmail", validateEmail));
  $("#join-userPhone").blur(() => checkDuplicate("#join-userPhone", "userPhone", validatePhone));

  //유효성
  $("#join-userPassword1").blur(() => handleValidation("#join-userPassword1", validatePassword));
  $("#join-userPassword2").blur(() => handleValidation("#join-userPassword2", validatePassword2));
  $("#join-userName").blur(() => handleValidation("#join-userName", validateName));
  $("#join-userBirthday").blur(() => handleValidation("#join-userBirthday", validateBirthday));

  //회원가입 버튼 클릭시 전체 유효성 검사 통과 여부 확인
  async function validateAll(callback) {
    const validations = [
      {id: "#join-userId", validator: validateId, key: "#join-userId"},
      {id: "#join-userPassword1", validator: validatePassword},
      {id: "#join-userName", validator: validateName},
      {id: "#join-userBirthday", validator: validateBirthday},
      {id: "#join-userEmail", validator: validateEmail, key: "#join-userEmail"},
      {id: "#join-userPhone", validator: validatePhone, key: "#join-userPhone"},
    ]

    // 1. 중복 확인부터 처리 (첫 실패 시 중단)
    for (const {id, key} of validations) {
      if (key && !duplicateCheckResults[key]) {
        $(id).css("border-color", "red");
        $(id).focus();
        alert("모두 입력해 주세요!");
        return false; // 중복 확인 실패 시 바로 중단
      }
    }

    // 2. 유효성 검사 처리 (첫 실패 시 중단)
    for (const {id, validator} of validations) {
      const isValid = handleValidation(id, validator);
      if (!isValid) {
        $(id).focus();
        alert("모든 필드의 형식을 맞추어야 합니다.");
        return false;
      }
    }

    // 3. 휴대폰 인증 완료하였는지 판단
    // 휴대폰 인증후 세션에 값저장한거 불러와서 거기서 true인지 false 인지 판단하는 검증소가 필요하다
    // ajax 통해서 검증소에서 반환하는 값으로 인증와료인지 아닌지 판단해야함
    const isVerified = await verifySmsCode(); // 결과를 기다림
    if (!isVerified) {
      alert("휴대폰 인증을 완료해야 합니다.");
      return false;
    }

    if (!$("#join-agreeAll").is(":checked")) {
      alert("모든 약관에 동의해야 합니다.");
      $("#join-agreeAll").focus();
      return false;
    }
    return true;
  }

//#endregion 유효성 및 중복확인 함수 모음

  //#region 편의성 함수 모음

  // 전체 동의 체크박스 클릭 시 하위 체크박스 상태 동기화
  $("#join-agreeAll").on("change", function () {
    const isChecked = $(this).is(":checked");
    $(".agreeBox").prop("checked", isChecked);
  });

  //하위 체크박스 상태 변경시 전체 동의 체크박스 업데이트
  $(".agreeBox").on("change", function () {
    // $(".agreeBox").length = agreeBox 인 체크박스 개수 반환
    // $(".agreeBox:checked").length = agreeBox 의 체크된 개수만 반환
    const isChecked = $(".agreeBox").length === $(".agreeBox:checked").length;
    $("#join-agreeAll").prop("checked", isChecked);
  })

  //이름 한글만 쓸수있게
  $("#join-userName").on("keydown", function (e) {
    const allowedKeys = ['Backspace', 'ArrowLeft', 'ArrowRight', 'Tab', 'Enter'];

    // 한글 입력 허용
    if (allowedKeys.includes(e.key) || (e.key >= '가' && e.key <= '힣')) {
      return true; // 정상 처리
    }
    else {
      e.preventDefault(); // 그 외 입력 차단
    }
  });

  //생년월일 숫자만 쓸수있고 년/월/일 형시으로 자동 포맷
  $('#join-userBirthday').on('input', function (e) {
    //숫자만 남기기
    let value = $(this).val().replace(/[^0-9]/g, '');

    // 백스페이스 시 포맷 처리 유연하게 유지
    if (e.originalEvent && e.originalEvent.inputType === 'deleteContentBackward') {
      return; // 기본 삭제 동작 유지
    }

    //4자리 후 '년' 추가
    if (value.length >= 4) {
      value = value.slice(0, 4) + '년' + value.slice(4);
    }
    //2자리 후 '월' 추가
    if (value.length >= 7) {
      value = value.slice(0, 7) + '월' + value.slice(7);
    }
    //마지막에 '일' 추가
    if (value.length >= 10) {
      value = value.slice(0, 10) + '일';
    }
    //최대 길이 제한
    $(this).val(value.slice(0, 11));
  });

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

  //휴대폰 인증 검증
  function verifySmsCode() {
    return new Promise((resolve, reject) => {
      $.ajax({
        type: "POST",
        url: "/login/verifySmsCode",
        success: (res) => {
          console.log(res.RESULT);
          resolve(res.RESULT); // 인증 결과 반환
        },
        error: (err) => {
          console.error(err);
          reject(false); // 실패 시 false 반환
        },
      });
    });
  }

  //#endregion 안녕

  //회원가입 버튼 클릭시
  $("#joinButton").on('click', async (e) => {
    e.preventDefault();
    const isValid = await validateAll();
    if (isValid) {
      //폼데이터 직렬화
      //const joinFormData = new FormData($("#joinForm")[0]);
      const formData = {};
      $("#joinForm").serializeArray().forEach(({ name, value }) => {
          formData[name] = value;
        });
      console.log(formData);

      $.ajax({
        type: "POST",
        url: "/login/join",
        data: JSON.stringify(formData),
        contentType: "application/json",
        success: function (response) {
          console.log("회원가입 성공");
          window.location.href = "/login";
        },
        error: function (xhr, status, error) {
          console.log("회원가입 실패" + error);
        }
      });
    }
    else console.log("회원가입 실패: 유효성 검사 또는 중복 확인 실패")
  });

});

