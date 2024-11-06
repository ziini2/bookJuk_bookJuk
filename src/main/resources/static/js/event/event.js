// "쿠폰 지급 조건" 및 관련 요소 삭제 함수
function removeCouponOptions() {
    $('#couponOptions').remove();         // "쿠폰 지급 조건" 드롭다운 삭제
    $('.coupon-divider').remove();        // 수평선 <hr> 요소 삭제
    $('#newUserPointField').remove();     // "신규 가입자" 포인트 입력 필드 삭제
	$('#manyTimesPointField').remove();   // "회 이상 대여자" 입력 필드 삭제
	$('#manyWonsPointField').remove();
}

// "신규 가입자" 포인트 입력 필드 삭제
function removeNewUserField() {
    $('#newUserPointField').remove();     // 포인트 입력 필드만 삭제
}

// "회 이상 대여자" 포인트 입력 필드 삭제
function manyTimesField() {
	$('#manyTimesPointField').remove();
}

function manyWonsField() {
	$('#manyWonsPointField').remove();
}

$(document).ready(function() {
	// datatables 라이브러리 설정
    const table = $('#event-table').DataTable({
        paging: true,
        searching: true,
        info: true,
		pageLength: 25,
        language: {
            search: "",
			info: "",
			infoEmpty: "",
			infoFiltered: "",
			infoPostFix: "",
			lengthMenu: 
               `<select class="event-select">
			   		<option value="25">25개씩 보기</option>
                    <option value="50">50개씩 보기</option>
                    <option value="100">100개씩 보기</option>
                    <option value="200">200개씩 보기</option>
                </select>`,
            paginate: {
                previous: "이전",
                next: "다음"
            }
        },
		dom: '<"d-flex justify-content-between align-items-end"<"d-flex align-items-end dataTables_filter_wrapper"f><"dataTables_length_wrapper"l>>rt<"d-flex justify-content-center"p>',
    });
	
	// 이벤트 검색 결과 수 상단에 표시
    $('.dataTables_length').before('<div id="event-searchResults" style="text-align: right;">&nbsp;</div>');

    // 이벤트 검색 결과 수 업데이트
    table.on('draw', function() {
        const info = table.page.info();
        $('#event-searchResults').text(`검색 결과 : ${info.recordsDisplay}개`);
    });

	$('#event-table_filter input').attr('placeholder', 'search');
	
	// 드롭박스(이벤트 검색 설정)
	$(".dataTables_filter").prepend(`
	    <select id="event-columnSelect" class="event-select ms-2" style="width: auto; display: inline;">
	        <option value="">전체</option>
	        <option value="0">NO</option>
	        <option value="1">이벤트 제목</option>
	        <option value="2">이벤트 유형</option>
	        <option value="3">이벤트 상태</option>
	        <option value="4">담당자</option>
	        <option value="5">이벤트 기간</option>
	    </select>
	`);
	
	// 이벤트 검색 필터 조건 나열
	$(".dataTables_filter").append(`
		<div class="d-flex align-items-center">
		    <button id="event-filterBtn" class="btn btn-primary me-2">필터</button>
		    <div id="event-selectedFilter"></div>
		</div>
	`);
	
	// 동적 이벤트 검색 기능 끄기
	$('#event-table_filter input').unbind();
	
	// 이벤트 검색
	$('#event-table_filter input').on('keypress', function(e) {
		if (e.key === 'Enter') {
			triggerSearch();
		}
	});
	
	$('#event-columnSelect').on('change', function() {
        $('#event-table_filter input').val('');
		table.search('').columns().search('').draw();
    });

	// 이벤트 검색 함수
	function triggerSearch() {
		const column = $('#event-columnSelect').val();
		const searchValue = $('#event-table_filter input').val();
		if (column) {
			table.column(column).search(searchValue).draw();
		} else {
			table.search(searchValue).draw();
		}
	}
});
$(document).ready(function () {
	const table = $('#event-table').DataTable();
	const eventTypeButtons = $('#eventType .event-filterModal-toggleBtn');
	const eventStatusButtons = $('#eventStatus .event-filterModal-toggleBtn');
	const eventDetailModal = $('#event-detailModal');
	let eventType = '';
	let eventStatus = '';
	let eventStartDate = '';
	let eventEndDate = '';
	
	$('#event-createModal-apply').click(function(){
		
	});
	
	$('#couponPayment').on('click', function() {
		// 이미 드롭다운이 생성되어 있으면 추가로 생성하지 않음
		if ($('#couponOptions').length === 0) {
	    // "쿠폰 지급 조건" 드롭다운을 동적으로 생성
		var couponOptions = `
			<hr class="coupon-divider">
			<div class="dropdown mt-2" id="couponOptions">
			<button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
				쿠폰 지급 조건
	        </button>
			<button type="button" class="btn-close ms-2" aria-label="Close" onclick="removeCouponOptions()"></button>
	        <ul class="dropdown-menu">
				<li><button id="newUserButton" class="dropdown-item">신규 가입자</button></li>
				<li><button id="manyTimesBtn" class="dropdown-item">회 이상 대여한 자</button></li>
				<li><button id="manyWons" class="dropdown-item">원 이상 대여한 자</button></li>
	        </ul>
			</div>
	    `;
	    
	    // "쿠폰 지급 조건" 드롭다운을 기존 드롭다운 아래에 추가
	    $(this).closest('.dropdown').after(couponOptions);
	  }
	});
	
	$(document).on('click', '#newUserButton', function() {
		if ($('#newUserPointField').length === 0) {
			const pointField = `
				<div class="d-flex align-items-center mt-2" id="newUserPointField">
					<span>신규 가입자에게 지급할 포인트 : </span>
	            	<input type="number" class="form-control ms-2" style="width: 80px;" placeholder="0"> <span class="ms-1">p</span>
	            	<button type="button" class="btn-close ms-2" aria-label="Close" onclick="removeNewUserField()"></button>
				</div>
	        `;
	        $('#couponOptions').after(pointField);
		}
	});
	
	$(document).on('click', '#manyTimesBtn', function() {
		const pointField = `
			<div class="d-flex align-items-center mt-2" id="manyTimesPointField">
				<input type="number" class="form-control ms-2" style="width: 40px;" placeholder="0">
				<span>회 이상 대여한 사람에게 지급할 포인트 : </span>
            	<input type="number" class="form-control ms-2" style="width: 80px;" placeholder="0"><span class="ms-1">p</span>
            	<button type="button" class="btn-close ms-2" aria-label="Close" onclick="manyTimesField()"></button>
			</div>
        `;
        $('#couponOptions').after(pointField);
	});
	
	$(document).on('click', '#manyWons', function() {
		const pointField = `
			<div class="d-flex align-items-center mt-2" id="manyWonsPointField">
				<input type="number" class="form-control ms-2" style="width: 80px;" placeholder="0">
				<span>원 이상 대여한 사람에게 지급할 포인트 : </span>
            	<input type="number" class="form-control ms-2" style="width: 80px;" placeholder="0"><span class="ms-1">p</span>
            	<button type="button" class="btn-close ms-2" aria-label="Close" onclick="manyWonsField()"></button>
			</div>
        `;
        $('#couponOptions').after(pointField);
	});
	  
  	$(document).on('click', '.close-coupon', function() {
        $('#couponOptions').remove();
		$('.coupon-divider').remove();
  	});
	
	$('#event-createBtn').click(function(){
		removeCouponOptions();
		$('#createEndDate').attr('min', '');
		$('#createStartDate').attr('max', '');
		$('#createStartDate').val('');
		$('#createEndDate').val('');
		$('#event-createModal-title').val('');
		$('#event-createModal-content textarea').val('');
		$('#event-createModal').fadeIn();
	});
	
	$('.event-modal-close').click(function(){
		$('#event-createModal').fadeOut();
	});
	
	$("#event-createModal-typeBtn").click(function() {
	    $("#event-createModal-typeDropdown").toggle();
	});
	
	$(".type-option").click(function() {
	    var type = $(this).data("type");
	    var button = $("<button>").text(type + " X").addClass("selected-type");
	    button.click(function() {
	      $(this).remove();
	    });
	    $("#selectedTypes").append(button);
	    $("#typeDropdown").hide();
	});
	
	// DataTables의 'draw' 이벤트에 이벤트 리스너 등록
    $('#event-table tbody').on('click', 'td span', function () {
        const cells = $(this).closest('tr').find('td');

        // 이벤트 상세 데이터 추출
        $('#event-detailTitle').text(cells.eq(1).text());
        $('#event-detailType').text(cells.eq(2).text());
        $('#event-detailStatus').text(cells.eq(3).text());
        $('#event-detailManager').text(cells.eq(4).text());
        $('#event-detailDate').text(cells.eq(5).text());
        eventDetailModal.show();
		
        // 이벤트 상세 모달 외부 클릭 시 닫기 이벤트 추가
        $(window).on('click.modalClose', function (event) {
            if ($(event.target).is(eventDetailModal)) {
                eventDetailModal.hide();
                $(window).off('click.modalClose');
            }
        });
    });
	
	// 이벤트 상세 모달창 닫기
	$('.close').on('click', function () {
        eventDetailModal.hide(); // 모달창 숨기기
        $(window).off('click.modalClose'); // 이벤트 제거
    });

	// 이벤트 검색 필터 모달창 내 이벤트 유형 버튼
	eventStatusButtons.click(function () {
		if ($(this).hasClass('active')) {
			$(this).removeClass('active');
			eventStatus = '';
		} else {
			eventStatusButtons.removeClass('active');
			$(this).addClass('active');
			eventStatus = $(this).data('value');
		}
	});

	// 이벤트 검색 필터 모달창 내 전송 상태 버튼
	eventTypeButtons.click(function () {
		if ($(this).hasClass('active')) {
			$(this).removeClass('active');
			eventType = '';
		} else {
			eventTypeButtons.removeClass('active');
			$(this).addClass('active');
			eventType = $(this).data('value');
		}
	});
  
	// 이벤트 검색 필터 모달창 내 전송 날짜 설정
	$.fn.dataTable.ext.search.push(function (settings, data) {
		const rowEventType = data[2];
		const rowEventStatus = data[3];
		const rowDateRange = data[5].split(" ~ ");
		const rowStartDate = new Date(rowDateRange[0]);
		const rowEndDate = new Date(rowDateRange[1]);
		const filterStartDate = eventStartDate ? new Date(eventStartDate) : null;
	    const filterEndDate = eventEndDate ? new Date(eventEndDate) : null;
		if (eventType && rowEventType !== eventType) return false;
		if (eventStatus && rowEventStatus !== eventStatus) return false;
		if (filterStartDate || filterEndDate) {
	        if (
	            (filterStartDate && filterStartDate <= rowEndDate && filterStartDate >= rowStartDate) || // 필터 시작 날짜가 이벤트 기간에 포함
	            (filterEndDate && filterEndDate >= rowStartDate && filterEndDate <= rowEndDate) || // 필터 종료 날짜가 이벤트 기간에 포함
	            (filterStartDate && filterEndDate && rowStartDate >= filterStartDate && rowEndDate <= filterEndDate) // 이벤트 기간이 필터 범위에 완전히 포함
	        ) {
	            return true;
	        }
	        return false;
	    }
		return true;
	});

	// 이벤트 검색 필터 버튼 눌렀을 때
	$('#event-filterBtn').click(() => {
		eventStatusButtons.removeClass('active');
		eventTypeButtons.removeClass('active');
		eventStatus = '';
		eventType = '';
		eventStartDate = '';
		eventEndDate = '';
		$('#eventEndDate').attr('min', '');
		$('#eventStartDate').attr('max', '');
		$('#eventStartDate').val('');
		$('#eventEndDate').val('');
    	$('#event-filterModal').css('display', 'block');
	});

	// 이벤트 검색 필터 모달창 닫기
	$('.event-modal-close').click(() => {
		$('#event-filterModal').css('display', 'none');
		$('#event-detailModal').css('display', 'none');
	});

	// 이벤트 검색 필터 모달창 내 완료 버튼
	$('#event-filterModal-apply').click(() => {
		eventStartDate = $('#eventStartDate').val();
    	eventEndDate = $('#eventEndDate').val();
    	displayAppliedFilters();
    	table.draw();
    	$('#event-filterModal').css('display', 'none');
	});
  
	// 이벤트 검색 필터 모달창 내 startDate 선택시 endDate의 최소값 제한 
	$('#eventStartDate').change(function () {
		const eventStartDateVal = $(this).val();
		$('#eventEndDate').attr('min', eventStartDateVal);
	});

	// 이벤트 검색 필터 모달창 내 endDate 선택시 startDate의 최대값 제한
	$('#eventEndDate').change(function () {
		const eventEndDateVal = $(this).val();
		$('#eventStartDate').attr('max', eventEndDateVal);
	});
	
	$('#createStartDate').change(function () {
		const createStartDateVal = $(this).val();
		$('#createEndDate').attr('min', createStartDateVal);
	});
	
	$('#createEndDate').change(function () {
		const createEndDateVal = $(this).val();
		$('#createStartDate').attr('max', createEndDateVal);
	});

	// 이벤트 검색 필터 모달창 내 선택된 버튼 이름 출력
	function displayAppliedFilters() {
		$('#event-selectedFilter').empty();
    	if (eventStatus) addFilterChip(eventStatus, 'eventStatus');
    	if (eventType) addFilterChip(eventType, 'eventType');
    	if (eventStartDate && eventEndDate) addFilterChip(`${eventStartDate} ~ ${eventEndDate}`, 'date');
	}

	// 이벤트 검색 필터 모달창 내 선택된 버튼 출력
	function addFilterChip(text, type) {
    	const chip = $('<div class="event-filterChip"></div>').text(text);
    	const closeBtn = $('<span>x</span>').click(() => removeFilter(type, text));
    	chip.append(closeBtn);
    	$('#event-selectedFilter').append(chip);
	}

	// 이벤트 검색 필터 모달창 내 값 초기화
	function removeFilter(type, text) {
		if (type === 'eventStatus') eventStatus = '';
    	if (type === 'eventType') eventType = '';
    	if (type === 'date') { eventStartDate = ''; eventEndDate = ''; }
    	displayAppliedFilters();
    	table.draw();
	}
});