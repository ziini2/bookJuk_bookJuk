$(document).ready(function() {
	
	// 메시지 보내기
	function sendNoti(recipientId) {
	    const notiData = {
	        recipient: recipientId,
	        content: "쪽지 내용",
	        type: "쪽지"
	    };

	    fetch('/admin/send', {
	        method: 'POST',
	        headers: {
	            'Content-Type': 'application/json'
	        },
	        body: JSON.stringify(notiData)
	    })
	    .then(response => response.ok ? alert("쪽지가 성공적으로 전송되었습니다.") : alert("쪽지 전송에 실패했습니다."))
	    .catch(error => console.error('Error:', error));
	}

	
	// datatables 라이브러리 설정
    const table = $('#noti-table').DataTable({
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
               `<select class="noti-select">
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
	
	// 알림 검색 결과 수 상단에 표시
    $('.dataTables_length').before('<div id="noti-searchResults" style="text-align: right;">&nbsp;</div>');

    // 알림 검색 결과 수 업데이트
    table.on('draw', function() {
        const info = table.page.info();
        $('#noti-searchResults').text(`검색 결과 : ${info.recordsDisplay}개`);
    });

	$('#noti-table_filter input').attr('placeholder', 'search');
	
	// 드롭박스(알림 검색 설정)
	$(".dataTables_filter").prepend(`
	    <select id="noti-columnSelect" class="noti-select ms-2" style="width: auto; display: inline;">
	        <option value="">전체</option>
	        <option value="0">NO</option>
	        <option value="1">수신 아이디</option>
	        <option value="2">알림 내용</option>
	        <option value="3">알림 유형</option>
	        <option value="4">전송 상태</option>
	        <option value="5">전송날짜</option>
	    </select>
	`);
	
	// 알림 검색 필터 조건 나열
	$(".dataTables_filter").append(`
		<div class="d-flex align-items-center">
		    <button id="noti-filterBtn" class="btn btn-primary me-2">필터</button>
		    <div id="noti-selectedFilter"></div>
		</div>
	`);
	
	// 동적 쿠폰 검색 기능 끄기
	$('#noti-table_filter input').unbind();
	
	// 쿠폰 검색
	$('#noti-table_filter input').on('keypress', function(e) {
		if (e.key === 'Enter') {
			triggerSearch();
		}
	});
	
	$('#noti-columnSelect').on('change', function() {
        $('#noti-table_filter input').val('');
		table.search('').columns().search('').draw();
    });

	// 쿠폰 검색 함수
	function triggerSearch() {
		const column = $('#noti-columnSelect').val();
		const searchValue = $('#noti-table_filter input').val();
		if (column) {
			table.column(column).search(searchValue).draw();
		} else {
			table.search(searchValue).draw();
		}
	}
});
$(document).ready(function () {
	const table = $('#noti-table').DataTable();
	const notiTypeButtons = $('#notiType .noti-filterModal-toggleBtn');
	const transferStatusButtons = $('#transferStatus .noti-filterModal-toggleBtn');
	const notiDetailModal = $('#noti-detailModal');
	let notiType = '';
	let transferStatus = '';
	let notiStartDate = '';
	let notiEndDate = '';
	
	// DataTables의 'draw' 이벤트에 이벤트 리스너 등록
    $('#noti-table tbody').on('click', 'td span', function () {
        const cells = $(this).closest('tr').find('td');

        // 알림 상세 데이터 추출
        $('#noti-detailReceiver').text(cells.eq(1).text());
        $('#noti-detailContent').text(cells.eq(2).text());
        $('#noti-detailType').text(cells.eq(3).text());
        $('#noti-detailStatus').text(cells.eq(4).text());
        $('#noti-detailSentDate').text(cells.eq(5).text());
        notiDetailModal.show();
		
        // 알림 상세 모달 외부 클릭 시 닫기 이벤트 추가
        $(window).on('click.modalClose', function (event) {
            if ($(event.target).is(notiDetailModal)) {
                notiDetailModal.hide();
                $(window).off('click.modalClose');
            }
        });
    });
	
	// 알림 상세 모달창 닫기
	$('.close').on('click', function () {
        notiDetailModal.hide(); // 모달창 숨기기
        $(window).off('click.modalClose'); // 이벤트 제거
    });

	// 알림 검색 필터 모달창 내 알림 유형 버튼
	notiTypeButtons.click(function () {
		if ($(this).hasClass('active')) {
			$(this).removeClass('active');
			notiType = '';
		} else {
			notiTypeButtons.removeClass('active');
			$(this).addClass('active');
			notiType = $(this).data('value');
		}
	});

	// 알림 검색 필터 모달창 내 전송 상태 버튼
	transferStatusButtons.click(function () {
		if ($(this).hasClass('active')) {
			$(this).removeClass('active');
			transferStatus = '';
		} else {
			transferStatusButtons.removeClass('active');
			$(this).addClass('active');
			transferStatus = $(this).data('value');
		}
	});
  
	// 알림 검색 필터 모달창 내 전송 날짜 설정
	$.fn.dataTable.ext.search.push(function (settings, data) {
		const rowNotiType = data[3];
		const rowTransferStatus = data[4];
		const rowDate = data[5];
		if (notiType && rowNotiType !== notiType) return false;
		if (transferStatus && rowTransferStatus !== transferStatus) return false;
		if (notiStartDate || notiEndDate) {
			const date = new Date(rowDate);
			if (notiStartDate && date < new Date(notiStartDate)) return false;
			if (notiEndDate && date > new Date(notiEndDate)) return false;
    	}
		return true;
	});

	// 알림 검색 필터 버튼 눌렀을 때
	$('#noti-filterBtn').click(() => {
		notiTypeButtons.removeClass('active');
		transferStatusButtons.removeClass('active');
		notiType = '';
		transferStatus = '';
		notiStartDate = '';
		notiEndDate = '';
		$('#notiEndDate').attr('min', '');
		$('#notiStartDate').attr('max', '');
		$('#notiStartDate').val('');
		$('#notiEndDate').val('');
    	$('#noti-filterModal').css('display', 'block');
	});

	// 알림 검색 필터 모달창 닫기
	$('.noti-modal-close').click(() => {
		$('#noti-filterModal').css('display', 'none');
		$('#noti-detailModal').css('display', 'none');
	});

	// 알림 검색 필터 모달창 내 완료 버튼
	$('#noti-filterModal-apply').click(() => {
		notiStartDate = $('#notiStartDate').val();
    	notiEndDate = $('#notiEndDate').val();
    	displayAppliedFilters();
    	table.draw();
    	$('#noti-filterModal').css('display', 'none');
	});
  
	// 알림 검색 필터 모달창 내 startDate 선택시 endDate의 최소값 제한 
	$('#notiStartDate').change(function () {
		const notiStartDateVal = $(this).val();
		$('#notiEndDate').attr('min', notiStartDateVal);
	});

	// 알림 검색 필터 모달창 내 endDate 선택시 startDate의 최대값 제한
	$('#notiEndDate').change(function () {
		const notiEndDateVal = $(this).val();
		$('#notiStartDate').attr('max', notiEndDateVal);
	});

	// 알림 검색 필터 모달창 내 선택된 버튼 이름 출력
	function displayAppliedFilters() {
		$('#noti-selectedFilter').empty();
    	if (notiType) addFilterChip(notiType, 'notiType');
    	if (transferStatus) addFilterChip(`전송 ${transferStatus}`, 'transferStatus');
    	if (notiStartDate && notiEndDate) addFilterChip(`${notiStartDate} ~ ${notiEndDate}`, 'date');
	}

	// 알림 검색 필터 모달창 내 선택된 버튼 출력
	function addFilterChip(text, type) {
    	const chip = $('<div class="noti-filterChip"></div>').text(text);
    	const closeBtn = $('<span>x</span>').click(() => removeFilter(type, text));
    	chip.append(closeBtn);
    	$('#noti-selectedFilter').append(chip);
	}

	// 알림 검색 필터 모달창 내 값 초기화
	function removeFilter(type, text) {
		if (type === 'notiType') notiType = '';
    	if (type === 'transferStatus') transferStatus = '';
    	if (type === 'date') { notiStartDate = ''; notiEndDate = ''; }
    	displayAppliedFilters();
    	table.draw();
	}
});