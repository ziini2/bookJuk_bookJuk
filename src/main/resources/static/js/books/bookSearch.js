function applyFilters() {
    const params = new URLSearchParams();

	const selectedStoreCode = document.getElementById('storeFilter').value;
	
	['rentalStatus', 'storeCode', 'search'].forEach(id => {
	    const value = document.getElementById(id)?.value;
	    if (value) params.append(id, value);
	});

    const url = `/admin/books?${params.toString()}`;
    window.location.href = url;
}
