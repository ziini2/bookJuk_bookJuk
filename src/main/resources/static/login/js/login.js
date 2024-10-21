
$(document).ready(function () {
  //유효성 검사 함수
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

    //유효성 검사 실패 시 중복 확인 하지 않음
    if(!handleValidation(formId, validator)){
      return;
    }

    $.ajax({
      type: "POST",
      url: "/checkData",
      contentType: "application/json",
      data: JSON.stringify({
        [dataKey]: value
      }),
      success: (res) => {
        if(res.RESULT === "SUCCESS"){
          console.log("중복확인 성공")
          $(formId).css("border-color", "#4ea685");
        }
        else{
          console.log("중복확인 실패")
          $(formId).css("border-color", "red");
        }
      },
      error: (err) => {
        console.error(err);
      }
    })
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

//==================================================================================================================

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