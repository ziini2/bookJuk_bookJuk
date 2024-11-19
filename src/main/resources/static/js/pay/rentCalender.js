$(document).ready(function() {
    // 오늘 날짜와 일주일 후 날짜 설정
    const today = new Date();
    const nextWeek = new Date();
    nextWeek.setDate(today.getDate() + 6); // 일주일 후 날짜 설정

    // Inline Datepicker 초기화
    $('#inline-datepicker').datepicker({
        format: 'yyyy-mm-dd',
        todayHighlight: true,
        autoclose: true,
        // 범위 설정
        startDate: today,
        endDate: nextWeek,
        beforeShowDay: function(date) {
            // 선택된 날짜가 nextWeek와 일치하면 활성화
            if (date.toDateString() === nextWeek.toDateString()) {
                return { enabled: true, classes: 'active' }; // active 클래스 추가
            }
            return { enabled: true }; // 다른 날짜는 선택 가능
        },
    }).datepicker('setDate', nextWeek) // Datepicker 초기화 시 activeDate를 일주일 뒤로 설정
      .datepicker('show'); // 페이지 로드 시 날짜 선택기 표시

    // 날짜 범위 선택 설정
    $('.input-group').datepicker({
        format: 'yyyy-mm-dd',
        todayHighlight: true,
        autoclose: true,
        // 범위 선택 기능 활성화
        inputs: $('.input1, .input2'),
        startDate: today, // 오늘 날짜로 설정
        endDate: nextWeek, // 일주일 후로 설정
    });

    // 시작 날짜와 종료 날짜를 초기값으로 설정
    $('.input1').val(today.toISOString().split('T')[0]); // ISO 형식으로 날짜 설정
    $('.input2').val(nextWeek.toISOString().split('T')[0]); // ISO 형식으로 날짜 설정
});
