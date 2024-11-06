$(document).ready(function() {
	// datatables 라이브러리 설정
    const table = $('#coupon-table').DataTable({
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
    });
	
	// 쿠폰 검색 결과 수 상단에 표시
    $('.dataTables_length').before('<div id="coupon-searchResults" style="text-align: right;">&nbsp;</div>');

    // 쿠폰 검색 결과 수 업데이트
    table.on('draw', function() {
        const info = table.page.info();
        $('#coupon-searchResults').text(`검색 결과 : ${info.recordsDisplay}개`);
    });

	$('#coupon-table_filter input').attr('placeholder', 'search');
	
	// 드롭박스(쿠폰 검색 설정)
	$(".dataTables_filter").prepend(`
	    <select id="coupon-columnSelect" class="coupon-select ms-2" style="width: auto; display: inline;">
	        <option value="">전체</option>
	        <option value="0">NO</option>
	        <option value="1">이벤트 제목</option>
	        <option value="2">유저 아이디</option>
	        <option value="3">쿠폰 상태</option>
	        <option value="4">쿠폰 종류</option>
	        <option value="5">쿠폰 유효기간</option>
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
		const column = $('#coupon-columnSelect').val();
		const searchValue = $('#coupon-table_filter input').val();
		if (column) {
			table.column(column).search(searchValue).draw();
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
    $('#coupon-table tbody').on('click', 'td span', function () {
        const cells = $(this).closest('tr').find('td');

        // 쿠폰 상세 데이터 추출
        $('#coupon-detailTitle').text(cells.eq(1).text());
        $('#coupon-detailUser').text(cells.eq(2).text());
        $('#coupon-detailStatus').text(cells.eq(3).text());
        $('#coupon-detailType').text(cells.eq(4).text());
        $('#coupon-detailDate').text(cells.eq(5).text());
        couponDetailModal.show();
		
        // 쿠폰 상세 모달 외부 클릭 시 닫기 이벤트 추가
        $(window).on('click.modalClose', function (event) {
            if ($(event.target).is(couponDetailModal)) {
                couponDetailModal.hide();
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
	$.fn.dataTable.ext.search.push(function (settings, data) {
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
    	const chip = $('<div class="coupon-filterChip"></div>').text(text);
    	const closeBtn = $('<span>x</span>').click(() => removeFilter(type, text));
    	chip.append(closeBtn);
    	$('#coupon-selectedFilter').append(chip);
	}

	// 쿠폰 검색 필터 모달창 내 값 초기화
	function removeFilter(type, text) {
		if (type === 'couponStatus') couponStatus = '';
    	if (type === 'couponType') couponType = '';
    	if (type === 'date') { couponStartDate = ''; couponEndDate = ''; }
    	displayAppliedFilters();
    	table.draw();
	}
});