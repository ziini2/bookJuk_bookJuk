$(document).ready(function () {
    const maxStores = 5;
    const revenueStoreList = window.globalState.revenueStoreList; // 포인트 분석용 전역 storeList 참조
    const delayStoreList = window.globalState.delayStoreList;     // 연체 분석용 전역 storeList 참조

    function populateDropdown(response, dropdownClass, dropdownItemClass) {
        const dropdown = $(dropdownClass);
        response.forEach(store => {
            dropdown.append(`<li><a class="${dropdownItemClass}" data-store="${store}">${store}</a></li>`);
        });
    }

    function handleDropdownClick(dropdownItem, buttonClass, selectedStoresContainerClass, storeList, requestDataFunc) {
        $(document).on('click', dropdownItem, function () {
            const storeName = $(this).data('store');
            const button = $(buttonClass);
            const selectedStoresContainer = $(selectedStoresContainerClass);

            if (storeName === "전체") {
                storeList.clear();
                $(dropdownItem).removeClass('active');
            } else if (storeList.has(storeName)) {
                storeList.delete(storeName);
                $(this).removeClass('active');
            } else {
                if (storeList.size < maxStores) {
                    storeList.add(storeName);
                    $(this).addClass('active');
                } else {
                    alert(`최대 ${maxStores}개의 지점만 선택할 수 있습니다.`);
                }
            }

            updateButtonText(button, storeList);
            updateSelectedStores(selectedStoresContainer, storeList);

            // 전역 requestData 함수 호출하여 최신 데이터를 요청
            if (typeof requestDataFunc === 'function') {
                requestDataFunc();
            }
        });
    }

    function updateButtonText(button, storeList) {
        if (storeList.size > 0) {
            button.text('지점: 선택중');
        } else {
            button.text('지점: 전체');
        }
    }

    function updateSelectedStores(selectedStoresContainer, storeList) {
        selectedStoresContainer.empty();

        if (storeList.size > 0) {
            storeList.forEach(store => {
                selectedStoresContainer.append(`<span class="badge bg-primary me-1">${store}</span>`);
            });
        } else {
            selectedStoresContainer.append('<span class="text-muted">선택된 지점이 없습니다.</span>');
        }
    }

    $.ajax({
        type: 'GET',
        url: '/admin/stores',
        dataType: 'json',
        success: function (response) {
            // 포인트 분석 드롭다운 구성
            populateDropdown(response, '.stores-revenue-dropdown', 'dropdown-item revenue-store');
            // 연체 분석 드롭다운 구성
            populateDropdown(response, '.stores-delay-dropdown', 'dropdown-item delay-store');

            // 포인트 분석 드롭다운 이벤트 처리
            handleDropdownClick(
                '.revenue-store',
                '.stores-btn.stores-revenue',
                '.selected-revenue-stores',
                revenueStoreList,
                window.globalState.requestData
            );

            // 연체 분석 드롭다운 이벤트 처리
            handleDropdownClick(
                '.delay-store',
                '.stores-btn.stores-delay',
                '.selected-delay-stores',
                delayStoreList,
                window.globalState.delayRequestData
            );
        },
        error: function (error) {
            console.error('Failed to fetch store list:', error);
        }
    });
});
