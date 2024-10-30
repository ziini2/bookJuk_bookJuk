$(document).ready(function() {
          // 오늘 날짜와 일주일 후 날짜 설정
          const today = new Date();
          const nextWeek = new Date();
          nextWeek.setDate(today.getDate() + 7);

          // Inline Datepicker 초기화
          $('#inline-datepicker').datepicker({
              format: 'yyyy-mm-dd',
              todayHighlight: true,
              autoclose: true,
          }).datepicker('show'); // 페이지 로드 시 날짜 선택기 표시

          // 날짜 범위 선택 설정
          $('.input-group').datepicker({
              format: 'yyyy-mm-dd',
              todayHighlight: true,
              autoclose: true,
              // 범위 선택 기능 활성화
              inputs: $('.input1, .input2'),
              // 시작 날짜와 종료 날짜 설정
              startDate: today, // 오늘 날짜로 설정
              endDate: nextWeek, // 일주일 후로 설정
          });

          // 시작 날짜와 종료 날짜를 초기값으로 설정
          $('.input1').val(today.toISOString().split('T')[0]); // ISO 형식으로 날짜 설정
          $('.input2').val(nextWeek.toISOString().split('T')[0]); // ISO 형식으로 날짜 설정
      });