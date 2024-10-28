//인증유효 시간 처리
let timerLeft = 30;

function updateTimer() {
  const minutes = Math.floor(timerLeft / 60);
  const seconds = timerLeft % 60;

  //남은 시간을 두자리 형식으로 표시 (ex: 01:02)
  const formattedTime = `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;

  //타이머에 표시
  $("#timer").text(`남은 시간 : ${formattedTime}`);

  //시간이 남아 있으면 1초씩 감소
  if (timerLeft > 0) {
    timerLeft --;
  }
  else {
    alert("시간이 만료 되었습니다. 다시 시도해 주세요")
    //여시 시간초 지낫으니까 다시 인증번호 적는 input 값 삭제 시키기
    $("#input-code").remove();
    $("#deleteCode").remove();
    $(".bx-phone").remove();
    $("#timer").remove();
    //에러메세지가 있는경우 에러메세지 삭제
    $(".error-message").remove();
    clearInterval(timer);
  }
}
