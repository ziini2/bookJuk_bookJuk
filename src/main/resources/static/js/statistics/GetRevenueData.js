$(document).ready(function () {

    let startDate;
    let endDate;
    let pointOption;
    let storeName;
    let page;
    let size;


    $.ajax({
        url: '/admin/statistics/revenue',
        method: 'GET',
        data: {
            startDate: startDate,
            endDate: endDate,
            pointOption: pointOption,
            storeName: storeName,
            page: page,
            size: size
        }

    })

});