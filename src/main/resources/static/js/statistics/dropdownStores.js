$(document).ready(function () {
    const maxStores = 5; // 선택 가능한 최대 지점 수

    // 공통 함수: 드롭다운 구성
    function populateDropdown(response, dropdownClass, dropdownItemClass) {
        const dropdown = $(dropdownClass);
        response.forEach(store => {
            dropdown.append(`<li><a class="${dropdownItemClass}" data-store="${store}">${store}</a></li>`);
        });
    }

    // 공통 함수: 선택된 지점 관리
    function handleDropdownClick(dropdownItem, buttonClass, selectedStoresContainerClass, selectedStoresSet) {
        $(document).on('click', dropdownItem, function () {
            const storeName = $(this).data('store');
            const button = $(buttonClass);
            const selectedStoresContainer = $(selectedStoresContainerClass);

            if (storeName === "전체") {
                selectedStoresSet.clear();
                $(dropdownItem).removeClass('active');
            } else if (selectedStoresSet.has(storeName)) {
                selectedStoresSet.delete(storeName);
                $(this).removeClass('active');
            } else {
                if (selectedStoresSet.size < maxStores) {
                    selectedStoresSet.add(storeName);
                    $(this).addClass('active');
                } else {
                    alert(`최대 ${maxStores}개의 지점만 선택할 수 있습니다.`);
                }
            }

            updateButtonText(button, selectedStoresSet);
            updateSelectedStores(selectedStoresContainer, selectedStoresSet);
        });
    }

    // 버튼 문구 업데이트
    function updateButtonText(button, selectedStoresSet) {
        if (selectedStoresSet.size > 0) {
            button.text('지점: 선택중');
        } else {
            button.text('지점: 전체');
        }
    }

    // 선택된 지점 표시 업데이트
    function updateSelectedStores(selectedStoresContainer, selectedStoresSet) {
        selectedStoresContainer.empty();

        if (selectedStoresSet.size > 0) {
            selectedStoresSet.forEach(store => {
                selectedStoresContainer.append(`<span class="badge bg-primary me-1">${store}</span>`);
            });
        } else {
            selectedStoresContainer.append('<span class="text-muted">선택된 지점이 없습니다.</span>');
        }
    }

    // AJAX 요청으로 데이터 가져오기
    $.ajax({
        type: 'GET',
        url: '/admin/stores',
        dataType: 'json',
        success: function (response) {
            // 매출항목 드롭다운 구성
            populateDropdown(response, '.stores-revenue-dropdown', 'dropdown-item revenue-store');
            populateDropdown(response, '.stores-delay-dropdown', 'dropdown-item delay-store');

            // 매출항목 드롭다운 이벤트 처리
            handleDropdownClick(
                '.revenue-store',
                '.stores-btn.stores-revenue',
                '.selected-revenue-stores',
                new Set()
            );

            // 연체항목 드롭다운 이벤트 처리
            handleDropdownClick(
                '.delay-store',
                '.stores-btn.stores-delay',
                '.selected-delay-stores',
                new Set()
            );
        },
        error: function (error) {
            console.error('Failed to fetch store list:', error);
        }
    });
});
