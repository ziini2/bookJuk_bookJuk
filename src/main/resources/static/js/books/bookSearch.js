function applyFilters() {
    const params = new URLSearchParams();

    // 'storeCode'의 선택 값을 추가
    const selectedStoreCode = document.getElementById('storeFilter').value;
    if (selectedStoreCode) {
        params.append('storeCode', selectedStoreCode);
    }

    // 'rentalStatus', 'search' 등의 추가 필터를 처리
    ['rentalStatus', 'search'].forEach(id => {
        const value = document.getElementById(id)?.value;
        if (value) {
            params.append(id, value);
        }
    });

    // 최종 URL로 리다이렉트
    const url = `/admin/books?${params.toString()}`;
    window.location.href = url;
}
