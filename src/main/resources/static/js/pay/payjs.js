$(document).ready(function() {
    $('.custom-date-picker .input-daterange').datepicker({
        format: 'yyyy-mm-dd', // 년-월-일 형식으로 날짜 표시
        autoclose: true
    });
});

$(".btnPay").on("click", function () {
    var userEmail = $("#userEmail").val(); //입력한 유저 이메일

    var merchant_uid = "O" + new Date().getTime(); // 고유한 결제번호 생성 
	// 사용자가 선택한 상품 코드 가져오기
	var pointAddPrice = $("input[name='pointAddPrice']:checked").val(); // 체크된 라디오 버튼의 value 값을 가져옴

	
    var IMP = window.IMP;	//imp 초기화
    IMP.init('imp68866867'); //고객사 식별코드

    IMP.request_pay({
        pg: "html5_inicis",           //등록된 pg사(KG이니시스)
        pay_method: "card",           //결제 방식: card(신용카드)
        merchant_uid: merchant_uid,   //결제번호
		userNum: userNum,			  //유저번호
        pointAddPrice: pointAddPrice, //충전할 포인트
        amount: totalAmount,          //결제금액
		
    }, function (rsp) {
        if (rsp.success) {
            var mesg = '결제가 완료되었습니다.';
            // 결제 정보 및 주문 정보 DB 저장 로직 추가
            alert(mesg);
        } else {
            var mesg = '결제가 실패하였습니다.';
            alert(mesg);
        }
    });
});

