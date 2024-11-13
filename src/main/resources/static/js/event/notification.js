// 날짜 형식을 YYYY-MM-DD 00:00:00 으로 변경
function dateChange(data) {
    const date = new Date(data);
    const formattedDate = date.toISOString().slice(0, 19).replace("T", " ");
    return formattedDate;
}

$(document).ready(function() {	
	// datatables 라이브러리 설정
    const table = $('#noti-table').DataTable({
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
    
		ajax: {
	        url: '/admin/getNoti',                   // 데이터 요청 URL
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
	                searchCriteria: $('#noti-columnSelect').val(),
	                searchKeyword: $('#noti-table_filter input').val(),
	                filter: $('#noti-selectedFilter .noti-filterChip').map(function() {
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
	        { data: 'notiId', title: 'No.' },
	        { data: 'recipient', title: '수신 아이디' },
	        { data: 'notiContent', title: '알림 내용' },
	        { data: 'notiType', title: '알림 유형' },
	        { data: 'transferStatus', title: '전송 상태' },
	        { data: 'notiSentDate', title: '전송 날짜', render: function(data) { return dateChange(data); } }
	    ]
		
	});
	
	// 알림 검색 결과 수 상단에 표시
    $('.dataTables_length').before('<div id="noti-searchResults" style="text-align: right;">&nbsp;</div>');

    // 알림 검색 결과 수 업데이트
    table.on('draw', function() {
        const info = table.page.info();
		page = table.page.info().page;
        $('#noti-searchResults').text(`검색 결과 : ${info.recordsDisplay}개`);
    });

	$('#noti-table_filter input').attr('placeholder', 'search');
	
	// 드롭박스(알림 검색 설정)
	$(".dataTables_filter").prepend(`
	    <select id="noti-columnSelect" class="noti-select ms-2" style="width: auto; display: inline;">
	        <option value="">전체</option>
	        <option value="notiId">NO</option>
	        <option value="recipient">수신 아이디</option>
	        <option value="notiContent">알림 내용</option>
	        <option value="notiType">알림 유형</option>
	        <option value="transferStatus">전송 상태</option>
	        <option value="notiSentDate">전송 날짜</option>
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
		const columnMap = {
		    "": null,
		    "notiId": 0,
		    "recipient": 1,
		    "notiContent": 2,
		    "notiType": 3,
		    "transferStatus": 4,
		    "notiSentDate": 5
		};
		const column = $('#noti-columnSelect').val();
		const columnIndex = columnMap[column];
		const searchValue = $('#noti-table_filter input').val();
		if (columnIndex !== null) {
		    table.column(columnIndex).search(searchValue).draw();
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
    $('#noti-table tbody').on('click', 'tr', function () {
		const rowData = table.row(this).data();  // 클릭된 행의 기본 데이터 가져오기
	    const notiId = rowData.notiId;  // 알림 ID 추출
	    $.ajax({
	        url: `/admin/noti/${notiId}`,  // RESTful 경로로 알림 ID 사용
	        method: 'GET',
	        success: function(data) {
	            // 가져온 데이터를 모달 창에 표시
	            $('#noti-detailReceiver').text(data.recipient);
	            $('#noti-detailContent').text(data.notiContent);
	            $('#noti-detailType').text(data.notiType);
	            $('#noti-detailStatus').text(data.transferStatus);
	            $('#noti-detailSentDate').text(dateChange(data.notiSentDate));

	            // 모달 창 표시
	            $('#noti-detailModal').show();
	        },
	        error: function(error) {
	            console.error("Error fetching noti details:", error);
	            alert("알림 세부 정보를 불러오는 데 오류가 발생했습니다.");
	        }
	    });

	    // 모달 외부 클릭 시 닫기 이벤트 추가
	    $(window).on('click.modalClose', function (event) {
	        if ($(event.target).is($('#noti-detailModal'))) {
	            $('#noti-detailModal').hide();
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
	$.fn.dataTable.ext.search.push(function (data) {
		
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
		
//		const rowNotiType = data[3];
//		const rowNotiStatus = data[4];
//		const rowDateRange = data[5].split(" ~ ");
//		const rowStartDate = new Date(rowDateRange[0]);
//		const rowEndDate = new Date(rowDateRange[1]);
//		const filterStartDate = notiStartDate ? new Date(notiStartDate) : null;
//	    const filterEndDate = notiEndDate ? new Date(notiEndDate) : null;
//		if (notiType && rowNotiType !== notiType) return false;
//		if (notiStatus && rowNotiStatus !== notiStatus) return false;
//		if (filterStartDate || filterEndDate) {
//	        if (
//	            (filterStartDate && filterStartDate <= rowEndDate && filterStartDate >= rowStartDate) || // 필터 시작 날짜가 기간에 포함
//	            (filterEndDate && filterEndDate >= rowStartDate && filterEndDate <= rowEndDate) || // 필터 종료 날짜가 기간에 포함
//	            (filterStartDate && filterEndDate && rowStartDate >= filterStartDate && rowEndDate <= filterEndDate) // 기간이 필터 범위에 완전히 포함
//	        ) {
//	            return true;
//	        }
//	        return false;
//	    }
//		return true;
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
    	if (transferStatus) addFilterChip(`${transferStatus}`, 'transferStatus');
    	if (notiStartDate && notiEndDate) addFilterChip(`${notiStartDate} ~ ${notiEndDate}`, 'date');
	}

	// 알림 검색 필터 모달창 내 선택된 버튼 출력
	function addFilterChip(text, type) {
    	const chip = $('<div class="noti-filterChip"></div>')
			.text(text)
			.attr('data-type', type)
	        .attr('data-value', text);
    	const closeBtn = $('<span>x</span>').click(() => removeFilter(type, text));
    	chip.append(closeBtn);
    	$('#noti-selectedFilter').append(chip);
	}

	// 알림 검색 필터 모달창 내 값 초기화
	function removeFilter(type, text) {
		if (type === 'notiType') notiType = '';
    	if (type === 'transferStatus') transferStatus = '';
    	if (type === 'date') { notiStartDate = ''; notiEndDate = ''; }
		$(`.noti-filterChip[data-type="${type}"][data-value="${text}"]`).remove();
    	table.draw();
	}
});