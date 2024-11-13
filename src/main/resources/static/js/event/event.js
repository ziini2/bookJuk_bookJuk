// "쿠폰 지급 조건" 및 관련 요소 삭제 함수
function removeCouponOptions() {
    $('#couponOptions').remove();         // "쿠폰 지급 조건" 드롭다운 삭제
    $('.coupon-divider').remove();        // 수평선 <hr> 요소 삭제
    $('#newUserPointField').remove();     // "신규 가입자" 포인트 입력 필드 삭제
	$('#manyTimesPointField').remove();   // "회 이상 대여자" 입력 필드 삭제
	$('#manyWonsPointField').remove();
	$('#manyPeoplePointField').remove();
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

function manyPeopleField() {
	$('#manyPeoplePointField').remove();
}

$(document).ready(function() {
	const table = $('#event-table').DataTable({
	    paging: true,
	    searching: true,
		processing: true,
	    pagingType: "full_numbers",
	    info: true,
	    serverSide: true,
	    pageLength: 25,
	    lengthMenu: [25, 50, 100, 200],
	    language: {
	        search: "",
	        info: "",
	        infoEmpty: "",
	        infoFiltered: "",
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
	    
	    ajax: {
	        url: '/admin/getEvent',                   // 데이터 요청 URL
	        type: 'POST',                             // HTTP 메서드 설정
	        contentType: 'application/json; charset=UTF-8',  // 요청 Content-Type 설정
	        dataSrc: 'data',                          // 데이터 소스 경로 설정
	        data: function(d) {
	            console.log(d);

	            // 요청 중단 조건: 데이터 길이가 0인 경우
	            if (d.length === 0) {
	                console.log("No more data to send.");
	                return false;
	            }

	            // 정렬 및 필터링 조건 설정
	            const order = d.order[0];
	            const columnIdx = order.column;
	            const sortDirection = order.dir;
	            const sortColumn = d.columns[columnIdx].data;

	            // 서버로 전송할 데이터 객체
	            return JSON.stringify({
	                searchCriteria: $('#event-columnSelect').val(),
	                searchKeyword: $('#event-table_filter input').val(),
	                filter: $('#event-selectedFilter .event-filterChip').map(function() {
	                    return { type: $(this).data('type'), value: $(this).data('value') };
	                }).get(),
	                start: d.start || 0,
	                length: d.length || 25,
	                draw: d.draw,
	                sortColumn: sortColumn,
	                sortDirection: sortDirection
	            });
	        },
			error: function(error, thrown) {
	            // AJAX 오류 처리
	            alert("서버 요청 중 오류가 발생했습니다.");
	            console.error("Error: ", error);
	            console.error("Thrown Error: ", thrown);
	        }
	    },
		
		order: [[0, 'desc']], // 여기서 기본 정렬 조건 설정 (0번째 컬럼을 내림차순으로 정렬)
	    
	    columns: [
	        { data: 'eventId', title: 'No.' },
	        { data: 'eventTitle', title: '이벤트 제목' },
	        { data: 'eventType', title: '이벤트 유형' },
	        { data: 'eventStatus', title: '이벤트 상태' },
	        { data: 'eventManager', title: '담당자' },
	        { data: 'eventDate', title: '이벤트 기간' },
			{ data: 'eventContent', title: '이벤트 내용', visible: false }
	    ]
	});

	//두번 연속 부르는걸 생각못함! 주석처리	
	$(document).on('change', '.event-select', function() {
		const pageSize = $(this).val();
//		table.page.len(pageSize).draw();
	});
		
	// 이벤트 검색 결과 수 상단에 표시
    $('.dataTables_length').before('<div id="event-searchResults" style="text-align: right;">&nbsp;</div>');

    // 이벤트 검색 결과 수 업데이트
    table.on('draw', function() {
        const info = table.page.info();
		page = table.page.info().page;
        $('#event-searchResults').text(`검색 결과 : ${info.recordsDisplay}개`);
    });

	$('#event-table_filter input').attr('placeholder', 'search');
	
	// 드롭박스(이벤트 검색 설정)
	$(".dataTables_filter").prepend(`
	    <select id="event-columnSelect" class="event-select ms-2" style="width: auto; display: inline;">
	        <option value="">전체</option>
	        <option value="eventId">NO</option>
	        <option value="eventTitle">이벤트 제목</option>
	        <option value="eventType">이벤트 유형</option>
	        <option value="eventStatus">이벤트 상태</option>
	        <option value="eventManager">담당자</option>
	        <option value="eventDate">이벤트 기간</option>
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
		const columnMap = {
		    "": null,
		    "eventId": 0,
		    "eventTitle": 1,
		    "eventType": 2,
		    "eventStatus": 3,
		    "eventManager": 4,
		    "eventDate": 5
		};
		const column = $('#event-columnSelect').val();
		const columnIndex = columnMap[column];
		const searchValue = $('#event-table_filter input').val();
		if (columnIndex !== null) {
		    table.column(columnIndex).search(searchValue).draw();
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
		const title = $('#event-createModal-title').val();
	    const startDate = $('#createStartDate').val();
	    const endDate = $('#createEndDate').val();
	    const content = $('#event-createModal-content textarea').val();
	    const firstButtonText = $('#couponOptions button:first').text().trim();
		const newUserPointField = $('#newUserPointField span').text().trim();
		const manyTimesPointField = $('#manyTimesPointField span').text().trim();
		const manyWonsPointField = $('#manyWonsPointField span').text().trim();
		const manyPeoplePointField = $('#manyPeoplePointField span').text().trim();
		const newUserPoint = $('#newUserPointField input').val();
		const manyTimesCount = $('#manyTimesPointField input').val();
		const manyTimesPoint = $('#manyTimesPointField input:eq(1)').val();
		const manyWonsPoint = $('#manyWonsPointField input').val();
		const manyWonsAmount = $('#manyWonsPointField input:eq(1)').val();
		const manyPeoplePoint = $('#manyPeoplePointField input').val();
		let eventConditions = [];

	    // 입력 검증
	    if (!title) {
	        alert('이벤트 제목을 입력해 주세요.');
	        return;
	    }
		
		if (!startDate) {
	        alert('이벤트 시작 날짜를 설정해 주세요.');
	        return;
	    }
		
		if (!endDate) {
	        alert('이벤트 종료 날짜를 설정해 주세요.');
	        return;
	    }
		
		if (!content) {
	        alert('이벤트 내용을 입력해 주세요.');
	        return;
	    }
		
		if (firstButtonText.length === 0) {
		    alert('이벤트 유형을 선택해주세요.');
			return;
		}
		
		if(newUserPointField.length === 0 &&
		   manyTimesPointField.length === 0 &&
		   manyWonsPointField.length === 0 &&
	   	   manyPeoplePointField.length === 0) {
			alert('쿠폰 지급 조건을 선택해주세요.');
			return;
		}
		
		if(newUserPointField.length !== 0){
			if(newUserPoint <= 0){
				alert('포인트를 입력해주세요.');
				return;
			}else{
				eventConditions.push({
					eventConditionType: "신규 가입자",
					eventClearReward: newUserPoint + "p"
				});
			}
		}
		
		if(manyTimesPointField.length !== 0){
			if(manyTimesCount <= 0){
				alert('대여 횟수를 입력해주세요.');
				return;
			}
			if(manyTimesPoint <= 0){
				alert('포인트를 입력해주세요.');
				return;
			}else{
				eventConditions.push({
					eventConditionType: manyTimesCount + "회 이상 대여한 회원",
					eventClearReward: manyTimesPoint + "p"
				});
			}
		}
	
		if(manyWonsPointField.length !== 0){
			if(manyWonsAmount <= 0){
				alert('금액을 입력해주세요.');
				return;
			}
			if(manyWonsPoint <= 0){
				alert('포인트를 입력해주세요.');
				return;
			}else{
				eventConditions.push({
					eventConditionType: manyWonsAmount + "원 이상 대여한 회원",
					eventClearReward: manyWonsPoint + "p"
				});
			}
		}
		
		if(manyPeoplePointField.length !== 0){
			if(manyPeoplePoint <= 0){
				alert('포인트를 입력해주세요.');
				return;
			}else{
				eventConditions.push({
					eventConditionType: "로그인한 회원",
					eventClearReward: manyPeoplePoint + "p"
				});
			}
		}
		
		const conditionsText = eventConditions.map(condition => {
	        return `${condition.eventConditionType}에게 ${condition.eventClearReward} 쿠폰 지급`;
	    }).join("\n");
		
		const confirmMessage = `이벤트를 생성하시겠습니까?\n\n이벤트 제목 : ${title}\n이벤트 기간 : ${startDate} ~ ${endDate}\n이벤트 조건 : ${conditionsText}\n이벤트 내용 : \n${content}`;
		if(confirm(confirmMessage)){
			$.ajax({
		        url: '/admin/eventCreate',
		        method: 'POST',
		        contentType: 'application/json',
		        data: JSON.stringify({
		            eventTitle: title,
					eventContent: content,
					eventType: firstButtonText.replace(" 조건", ""),
					startEventDate: startDate,
					endEventDate: endDate,
					eventCondition: eventConditions
		        }),
		        success: function (response) {
					if(response.result === "success"){
			            alert("이벤트가 성공적으로 생성되었습니다.");
			            table.draw();
						$('#event-createModal').fadeOut();
					}else{
						alert("이벤트가 생성에 실패하였습니다.")
					}
		        },
		        error: function (error) {
					alert("오류: " + error.responseText);
		            console.error("이벤트 생성 실패:", error);
		        }
		    });
		}
	});
	
	$('#couponPayment').on('click', function() {
		// 이미 드롭다운이 생성되어 있으면 추가로 생성하지 않음
		if ($('#couponOptions').length === 0) {
	    // "쿠폰 지급 조건" 드롭다운을 동적으로 생성
		var couponOptions = `
			<hr class="coupon-divider">
			<div class="dropdown mt-2" id="couponOptions">
			<button id="coupon" class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
				쿠폰 지급 조건
	        </button>
			<button type="button" class="btn-close ms-2" aria-label="Close" onclick="removeCouponOptions()"></button>
	        <ul class="dropdown-menu">
				<li><button id="newUserButton" class="dropdown-item">신규 가입자</button></li>
				<li><button id="manyTimesBtn" class="dropdown-item">회 이상 대여한 자</button></li>
				<li><button id="manyWons" class="dropdown-item">원 이상 대여한 자</button></li>
				<li><button id="manyPeople" class="dropdown-item">로그인한 회원</button></li>
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
		if ($('#manyTimesPointField').length === 0) {
			const pointField = `
				<div class="d-flex align-items-center mt-2" id="manyTimesPointField">
					<input type="number" class="form-control ms-2" style="width: 40px;" placeholder="0">
					<span>회 이상 대여한 회원에게 지급할 포인트 : </span>
		        	<input type="number" class="form-control ms-2" style="width: 80px;" placeholder="0"><span class="ms-1">p</span>
		        	<button type="button" class="btn-close ms-2" aria-label="Close" onclick="manyTimesField()"></button>
				</div>
		    `;
		    $('#couponOptions').after(pointField);
		}
	});
	
	$(document).on('click', '#manyWons', function() {
		if ($('#manyWonsPointField').length === 0) {
			const pointField = `
				<div class="d-flex align-items-center mt-2" id="manyWonsPointField">
					<input type="number" class="form-control ms-2" style="width: 80px;" placeholder="0">
					<span>원 이상 대여한 회원에게 지급할 포인트 : </span>
		        	<input type="number" class="form-control ms-2" style="width: 80px;" placeholder="0"><span class="ms-1">p</span>
		        	<button type="button" class="btn-close ms-2" aria-label="Close" onclick="manyWonsField()"></button>
				</div>
		    `;
		    $('#couponOptions').after(pointField);
		}
	});
	
	$(document).on('click', '#manyPeople', function() {
		if ($('#manyPeoplePointField').length === 0) {
			const pointField = `
				<div class="d-flex align-items-center mt-2" id="manyPeoplePointField">
					<span>로그인한 회원에게 지급할 포인트 : </span>
					<input type="number" class="form-control ms-2" style="width: 80px;" placeholder="0"><span class="ms-1">p</span>
		        	<button type="button" class="btn-close ms-2" aria-label="Close" onclick="manyPeopleField()"></button>
				</div>
		    `;
		    $('#couponOptions').after(pointField);
		}
	});
	  
  	$(document).on('click', '.close-coupon', function() {
        $('#couponOptions').remove();
		$('.coupon-divider').remove();
  	});
	
	$('#event-createBtn').click(function(){
		const today = new Date();
		const tomorrow = new Date(today);
		tomorrow.setDate(today.getDate() + 1);

		// yyyy-mm-dd 형식으로 변환
		const year = tomorrow.getFullYear();
		const month = String(tomorrow.getMonth() + 1).padStart(2, '0');
		const day = String(tomorrow.getDate()).padStart(2, '0');
		const tomorrowDate = `${year}-${month}-${day}`;
		removeCouponOptions();
		$('#createEndDate').attr('min', '');
		$('#createStartDate').attr('max', '');
		$('#createStartDate').attr('min', tomorrowDate);
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
	
	$('#event-table tbody').on('click', 'tr', function () {
	    const rowData = table.row(this).data();  // 클릭된 행의 기본 데이터 가져오기
	    const eventId = rowData.eventId;  // 이벤트 ID 추출
	    // Ajax 요청으로 상세 정보 가져오기
	    $.ajax({
	        url: `/admin/event/${eventId}`,  // RESTful 경로로 이벤트 ID 사용
	        method: 'GET',
	        success: function(data) {
	            // 가져온 데이터를 모달 창에 표시
	            $('#event-detailTitle').text(data.eventTitle);
	            $('#event-detailType').text(data.eventType);
	            $('#event-detailStatus').text(data.eventStatus);
	            $('#event-detailManager').text(data.eventManager);
	            $('#event-detailDate').text(data.eventDate);
	            $('#event-detailContent').text(data.eventContent);

	            // 조건 목록 출력
	            $('#event-detailCondition').html(data.eventCondition
	                .map(condition => `[${condition.eventConditionType} : ${condition.eventRequiredValue}] [${condition.eventClearReward} 지급]`)
	                .join('<br>'));

	            $('#event-detailCreationDate').text(data.eventCreationDate.split('T')[0]);

	            // 모달 창 표시
	            $('#event-detailModal').show();
	        },
	        error: function(error) {
	            console.error("Error fetching event details:", error);
	            alert("이벤트 세부 정보를 불러오는 데 오류가 발생했습니다.");
	        }
	    });

	    // 모달 외부 클릭 시 닫기 이벤트 추가
	    $(window).on('click.modalClose', function (event) {
	        if ($(event.target).is($('#event-detailModal'))) {
	            $('#event-detailModal').hide();
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

	// 이벤트 검색 필터 모달창 내 이벤트 상태 버튼
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
	$.fn.dataTable.ext.search.push(function (data) {
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
		$('#eventStartDate').attr('min', '');
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
    	const chip = $('<div class="event-filterChip"></div>')
			.text(text)
			.attr('data-type', type)
	        .attr('data-value', text);
    	const closeBtn = $('<span>x</span>').click(() => removeFilter(type, text));
    	chip.append(closeBtn);
    	$('#event-selectedFilter').append(chip);
	}

	// 이벤트 검색 필터 모달창 내 값 초기화
	function removeFilter(type, text) {
		if (type === 'eventStatus') eventStatus = '';
    	if (type === 'eventType') eventType = '';
    	if (type === 'date') { eventStartDate = ''; eventEndDate = ''; }
		$(`.event-filterChip[data-type="${type}"][data-value="${text}"]`).remove();
//    	displayAppliedFilters();
    	table.draw();
	}
});