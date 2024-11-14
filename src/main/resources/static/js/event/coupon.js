// 날짜 형식을 YYYY-MM-DD 00:00:00 으로 변경
function dateChange(data) {
    const date = new Date(data);
    const formattedDate = date.toISOString().slice(0, 10);
    return formattedDate;
}

$(document).ready(function() {
	// datatables 라이브러리 설정
    const table = $('#coupon-table').DataTable({
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
               `<select class="coupon-select">
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
	        url: '/admin/getCoupon',                   // 데이터 요청 URL
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
	                searchCriteria: $('#coupon-columnSelect').val(),
	                searchKeyword: $('#coupon-table_filter input').val(),
	                filter: $('#coupon-selectedFilter .coupon-filterChip').map(function() {
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
	            alert("서버 요청 중 오류가 발생했습니다.");
	            console.error("Error: ", error);
	            console.error("Thrown Error: ", thrown);
	        }
	    },
		
		order: [[0, 'desc']], // 여기서 기본 정렬 조건 설정 (0번째 컬럼을 내림차순으로 정렬)
	    
	    columns: [
	        { data: 'couponId', title: 'No.' },
	        { data: 'eventTitle', title: '이벤트 제목' },
	        { data: 'userId', title: '유저 아이디' },
	        { data: 'couponStatus', title: '쿠폰 상태' },
	        { data: 'couponType', title: '쿠폰 종류' },
	        { data: 'couponPeriod', title: '쿠폰 유효기간', render: function(data) { return dateChange(data); } }
	    ]		
	});
	
	// 쿠폰 검색 결과 수 상단에 표시
    $('.dataTables_length').before('<div id="coupon-searchResults" style="text-align: right;">&nbsp;</div>');

    // 쿠폰 검색 결과 수 업데이트
    table.on('draw', function() {
        const info = table.page.info();
		page = table.page.info().page;
        $('#coupon-searchResults').text(`검색 결과 : ${info.recordsDisplay}개`);
    });

	$('#coupon-table_filter input').attr('placeholder', 'search');
	
	// 드롭박스(쿠폰 검색 설정)
	$(".dataTables_filter").prepend(`
	    <select id="coupon-columnSelect" class="coupon-select ms-2" style="width: auto; display: inline;">
	        <option value="">전체</option>
	        <option value="couponId">NO</option>
	        <option value="eventTitle">이벤트 제목</option>
	        <option value="userId">유저 아이디</option>
	        <option value="couponStatus">쿠폰 상태</option>
	        <option value="couponType">쿠폰 종류</option>
	        <option value="couponPeriod">쿠폰 유효기간</option>
	    </select>
	`);
	
	// 쿠폰 검색 필터 조건 나열
	$(".dataTables_filter").append(`
		<div class="d-flex align-items-center">
		    <button id="coupon-filterBtn" class="btn btn-primary me-2">필터</button>
		    <div id="coupon-selectedFilter"></div>
		</div>
	`);
	
	// 동적 쿠폰 검색 기능 끄기
	$('#coupon-table_filter input').unbind();
	
	// 쿠폰 검색
	$('#coupon-table_filter input').on('keypress', function(e) {
		if (e.key === 'Enter') {
			triggerSearch();
		}
	});
	
	$('#coupon-columnSelect').on('change', function() {
        $('#coupon-table_filter input').val('');
		table.search('').columns().search('').draw();
    });

	// 쿠폰 검색 함수
	function triggerSearch() {
		const columnMap = {
		    "": null,
		    "couponId": 0,
		    "eventTitle": 1,
		    "userId": 2,
		    "couponStatus": 3,
		    "couponType": 4,
		    "couponPeriod": 5
		};
		const column = $('#coupon-columnSelect').val();
		const columnIndex = columnMap[column];
		const searchValue = $('#coupon-table_filter input').val();
		if (columnIndex !== null) {
		    table.column(columnIndex).search(searchValue).draw();
		} else {
		    table.search(searchValue).draw();
		}
	}
});
$(document).ready(function () {
	const table = $('#coupon-table').DataTable();
	const couponStatusButtons = $('#couponStatus .coupon-filterModal-toggleBtn');
	const couponTypeButtons = $('#couponType .coupon-filterModal-toggleBtn');
	const couponDetailModal = $('#coupon-detailModal');
	let couponStatus = '';
	let couponType = '';
	let couponStartDate = '';
	let couponEndDate = '';
	
	// DataTables의 'draw' 이벤트에 이벤트 리스너 등록
    $('#coupon-table tbody').on('click', 'tr', function () {
		const rowData = table.row(this).data();  // 클릭된 행의 기본 데이터 가져오기
	    const couponId = rowData.couponId;  // 알림 ID 추출
	    $.ajax({
	        url: `/admin/coupon/${couponId}`,  // RESTful 경로로 알림 ID 사용
	        method: 'GET',
	        success: function(data) {
	            // 가져온 데이터를 모달 창에 표시
	            $('#coupon-detailTitle').text(data.eventTitle);
	            $('#coupon-detailUser').text(data.userId);
	            $('#coupon-detailStatus').text(data.couponStatus);
	            $('#coupon-detailType').text(data.couponType);
				$('#coupon-detailNum').text(data.couponNum);
	            $('#coupon-detailDate').text(dateChange(data.couponPeriod));

	            // 모달 창 표시
	            $('#coupon-detailModal').show();
	        },
	        error: function(error) {
	            console.error("Error fetching noti details:", error);
	            alert("알림 세부 정보를 불러오는 데 오류가 발생했습니다.");
	        }
	    });

	    // 모달 외부 클릭 시 닫기 이벤트 추가
	    $(window).on('click.modalClose', function (event) {
	        if ($(event.target).is($('#coupon-detailModal'))) {
	            $('#coupon-detailModal').hide();
	            $(window).off('click.modalClose');
	        }
	    });
    });
	
	// 쿠폰 상세 모달창 닫기
	$('.close').on('click', function () {
        couponDetailModal.hide(); // 모달창 숨기기
        $(window).off('click.modalClose'); // 이벤트 제거
    });

	// 쿠폰 검색 필터 모달창 내 쿠폰 유형 버튼
	couponStatusButtons.click(function () {
		if ($(this).hasClass('active')) {
			$(this).removeClass('active');
			couponStatus = '';
		} else {
			couponStatusButtons.removeClass('active');
			$(this).addClass('active');
			couponStatus = $(this).data('value');
		}
	});

	// 쿠폰 검색 필터 모달창 내 전송 상태 버튼
	couponTypeButtons.click(function () {
		if ($(this).hasClass('active')) {
			$(this).removeClass('active');
			couponType = '';
		} else {
			couponTypeButtons.removeClass('active');
			$(this).addClass('active');
			couponType = $(this).data('value');
		}
	});
  
	// 쿠폰 검색 필터 모달창 내 전송 날짜 설정
	$.fn.dataTable.ext.search.push(function (data) {
		
		const rowCouponStatus = data[3];
		const rowCouponType = data[4];
		const rowDate = data[5];
		if (couponStatus && rowCouponStatus !== couponStatus) return false;
		if (couponType) {
			if (couponType === '포인트' && !rowCouponType.endsWith('p')) return false;
		}
		if (couponStartDate || couponEndDate) {
			const date = new Date(rowDate);
			if (couponStartDate && date < new Date(couponStartDate)) return false;
			if (couponEndDate && date > new Date(couponEndDate)) return false;
    	}
		return true;
	});

	// 쿠폰 검색 필터 버튼 눌렀을 때
	$('#coupon-filterBtn').click(() => {
		couponStatusButtons.removeClass('active');
		couponTypeButtons.removeClass('active');
		couponStatus = '';
		couponType = '';
		couponStartDate = '';
		couponEndDate = '';
		$('#couponEndDate').attr('min', '');
		$('#couponStartDate').attr('max', '');
		$('#couponStartDate').val('');
		$('#couponEndDate').val('');
    	$('#coupon-filterModal').css('display', 'block');
	});

	// 쿠폰 검색 필터 모달창 닫기
	$('.coupon-modal-close').click(() => {
		$('#coupon-filterModal').css('display', 'none');
		$('#coupon-detailModal').css('display', 'none');
	});

	// 쿠폰 검색 필터 모달창 내 완료 버튼
	$('#coupon-filterModal-apply').click(() => {
		couponStartDate = $('#couponStartDate').val();
    	couponEndDate = $('#couponEndDate').val();
    	displayAppliedFilters();
    	table.draw();
    	$('#coupon-filterModal').css('display', 'none');
	});
  
	// 쿠폰 검색 필터 모달창 내 startDate 선택시 endDate의 최소값 제한 
	$('#couponStartDate').change(function () {
		const couponStartDateVal = $(this).val();
		$('#couponEndDate').attr('min', couponStartDateVal);
	});

	// 쿠폰 검색 필터 모달창 내 endDate 선택시 startDate의 최대값 제한
	$('#couponEndDate').change(function () {
		const couponEndDateVal = $(this).val();
		$('#couponStartDate').attr('max', couponEndDateVal);
	});

	// 쿠폰 검색 필터 모달창 내 선택된 버튼 이름 출력
	function displayAppliedFilters() {
		$('#coupon-selectedFilter').empty();
    	if (couponStatus) addFilterChip(couponStatus, 'couponStatus');
    	if (couponType) addFilterChip('포인트', 'couponType');
    	if (couponStartDate && couponEndDate) addFilterChip(`${couponStartDate} ~ ${couponEndDate}`, 'date');
	}

	// 쿠폰 검색 필터 모달창 내 선택된 버튼 출력
	function addFilterChip(text, type) {
		const chip = $('<div class="coupon-filterChip"></div>')
			.text(text)
			.attr('data-type', type)
	        .attr('data-value', text);
    	const closeBtn = $('<span>x</span>').click(() => removeFilter(type, text));
    	chip.append(closeBtn);
    	$('#coupon-selectedFilter').append(chip);
	}

	// 쿠폰 검색 필터 모달창 내 값 초기화
	function removeFilter(type, text) {
		if (type === 'couponStatus') couponStatus = '';
    	if (type === 'couponType') couponType = '';
    	if (type === 'date') { couponStartDate = ''; couponEndDate = ''; }
    	$(`.coupon-filterChip[data-type="${type}"][data-value="${text}"]`).remove();
    	table.draw();
	}
});