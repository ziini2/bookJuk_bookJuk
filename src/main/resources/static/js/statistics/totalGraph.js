document.addEventListener('DOMContentLoaded', function() {
    const margin = {top: 20, right: 40, bottom: 30, left: 50},
        width = 1200 - margin.left - margin.right,
        height = 350 - margin.top - margin.bottom;

    const parseTime = d3.timeParse("%d-%b-%y");

    const x = d3.scaleTime().range([0, width]);
    const y = d3.scaleLinear().range([height, 0]);

    const svg = d3.select(".total_short_cut_chart").append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", `translate(${margin.left},${margin.top})`);

    let data = [
        {date: "1-May-12", store: "store1", revenue: 58.13, rental: 40, newUser: 20, totalUser: 200},
        {date: "30-Apr-12", store: "store1", revenue: 53.98, rental: 45, newUser: 30, totalUser: 230},
        {date: "27-Apr-12", store: "store2", revenue: 67.00, rental: 50, newUser: 40, totalUser: 270},
        {date: "26-Apr-12", store: "store2", revenue: 89.70, rental: 55, newUser: 35, totalUser: 305},
        {date: "25-Apr-12", store: "store3", revenue: 99.00, rental: 60, newUser: 50, totalUser: 355},
        {date: "24-Apr-12", store: "store3", revenue: 130.28, rental: 65, newUser: 55, totalUser: 410},
        {date: "23-Apr-12", store: "store1", revenue: 166.70, rental: 70, newUser: 60, totalUser: 470},
        {date: "22-Apr-12", store: "store1", revenue: 120.70, rental: 75, newUser: 45, totalUser: 515},
        {date: "21-Apr-12", store: "store2", revenue: 98.10, rental: 80, newUser: 70, totalUser: 585},
        {date: "20-Apr-12", store: "store3", revenue: 234.98, rental: 90, newUser: 75, totalUser: 660},
        {date: "19-Apr-12", store: "store2", revenue: 345.44, rental: 100, newUser: 80, totalUser: 740},
        {date: "18-Apr-12", store: "store1", revenue: 443.34, rental: 110, newUser: 85, totalUser: 825},
        {date: "17-Apr-12", store: "store1", revenue: 543.70, rental: 120, newUser: 90, totalUser: 915},
        {date: "16-Apr-12", store: "store2", revenue: 580.13, rental: 125, newUser: 95, totalUser: 1010},
        {date: "15-Apr-12", store: "store2", revenue: 510.13, rental: 100, newUser: 85, totalUser: 1095},
        {date: "14-Apr-12", store: "store3", revenue: 490.20, rental: 110, newUser: 70, totalUser: 1165},
        {date: "13-Apr-12", store: "store3", revenue: 605.23, rental: 130, newUser: 75, totalUser: 1240},
        {date: "12-Apr-12", store: "store1", revenue: 622.77, rental: 140, newUser: 85, totalUser: 1325},
        {date: "11-Apr-12", store: "store1", revenue: 626.20, rental: 145, newUser: 90, totalUser: 1415},
        {date: "10-Apr-12", store: "store2", revenue: 628.44, rental: 150, newUser: 95, totalUser: 1510},
        {date: "9-Apr-12", store: "store2", revenue: 636.23, rental: 155, newUser: 100, totalUser: 1610},
        {date: "8-Apr-12", store: "store3", revenue: 640.30, rental: 160, newUser: 105, totalUser: 1715},
        {date: "7-Apr-12", store: "store3", revenue: 635.20, rental: 158, newUser: 102, totalUser: 1817},
        {date: "6-Apr-12", store: "store1", revenue: 610.45, rental: 140, newUser: 85, totalUser: 1902},
        {date: "5-Apr-12", store: "store1", revenue: 633.68, rental: 162, newUser: 108, totalUser: 2010},
        {date: "4-Apr-12", store: "store2", revenue: 624.31, rental: 155, newUser: 95, totalUser: 2105},
        {date: "3-Apr-12", store: "store2", revenue: 629.32, rental: 150, newUser: 92, totalUser: 2197},
        {date: "2-Apr-12", store: "store3", revenue: 618.63, rental: 140, newUser: 90, totalUser: 2287},
        {date: "1-Apr-12", store: "store3", revenue: 620.90, rental: 145, newUser: 88, totalUser: 2375},
        {date: "31-Mar-12", store: "store1", revenue: 599.55, rental: 135, newUser: 83, totalUser: 2458},
        {date: "30-Mar-12", store: "store1", revenue: 609.86, rental: 138, newUser: 85, totalUser: 2543},
        {date: "29-Mar-12", store: "store2", revenue: 617.62, rental: 142, newUser: 89, totalUser: 2632},
        {date: "28-Mar-12", store: "store2", revenue: 614.48, rental: 140, newUser: 87, totalUser: 2719},
        {date: "27-Mar-12", store: "store3", revenue: 606.98, rental: 135, newUser: 84, totalUser: 2803},
        {date: "26-Mar-12", store: "store3", revenue: 620.34, rental: 145, newUser: 92, totalUser: 2895},
        {date: "25-Mar-12", store: "store1", revenue: 635.22, rental: 150, newUser: 95, totalUser: 2990},
        {date: "24-Mar-12", store: "store1", revenue: 645.45, rental: 155, newUser: 97, totalUser: 3087},
        {date: "23-Mar-12", store: "store2", revenue: 652.60, rental: 160, newUser: 100, totalUser: 3187},
        {date: "22-Mar-12", store: "store2", revenue: 660.22, rental: 165, newUser: 103, totalUser: 3290},
        {date: "21-Mar-12", store: "store3", revenue: 670.80, rental: 170, newUser: 110, totalUser: 3400},
        {date: "20-Mar-12", store: "store3", revenue: 680.50, rental: 175, newUser: 112, totalUser: 3512},
        {date: "19-Mar-12", store: "store1", revenue: 690.75, rental: 180, newUser: 115, totalUser: 3627},
        {date: "18-Mar-12", store: "store1", revenue: 702.33, rental: 185, newUser: 118, totalUser: 3745},
        {date: "17-Mar-12", store: "store2", revenue: 710.95, rental: 190, newUser: 120, totalUser: 3865},
        {date: "16-Mar-12", store: "store2", revenue: 720.13, rental: 195, newUser: 125, totalUser: 3990},
        {date: "15-Mar-12", store: "store3", revenue: 730.18, rental: 200, newUser: 130, totalUser: 4120},
        {date: "14-Mar-12", store: "store3", revenue: 740.45, rental: 205, newUser: 132, totalUser: 4252},
        {date: "13-Mar-12", store: "store1", revenue: 750.50, rental: 210, newUser: 135, totalUser: 4387},
        {date: "12-Mar-12", store: "store1", revenue: 760.60, rental: 215, newUser: 140, totalUser: 4527}
    ];


    data.forEach(d => {
        d.date = parseTime(d.date);
        d.revenue = +d.revenue;
        d.rental = +d.rental;
        d.newUser = +d.newUser;
        d.totalUser = +d.totalUser;
    });

    function updateGraph(selectedYValue, selectedStore) {
        const valueline = d3.line()
            .x(d => x(d.date))
            .y(d => y(d[selectedYValue]));

        let filteredData = data;
        if (selectedStore !== 'all') {
            filteredData = data.filter(d => d.store === selectedStore);
        }

        x.domain(d3.extent(filteredData, d => d.date));
        y.domain([0, d3.max(filteredData, d => d[selectedYValue]) * 1.1]);

        const line = svg.selectAll(".line")
            .data([filteredData]);

        line.enter()
            .append("path")
            .attr("class", "line")
            .merge(line)
            .transition()
            .duration(1000)
            .attr("d", valueline);

        line.exit().remove();

        const xAxis = d3.axisBottom(x);
        const yAxis = d3.axisLeft(y);

        svg.selectAll(".x-axis").remove();
        svg.append("g")
            .attr("class", "x-axis axis")
            .attr("transform", `translate(0,${height})`)
            .transition()
            .duration(1000)
            .call(xAxis);

        svg.selectAll(".y-axis").remove();
        svg.append("g")
            .attr("class", "y-axis axis")
            .transition()
            .duration(1000)
            .call(yAxis);
    }

    updateGraph('revenue', 'all');

    // 이벤트 리스너를 순수 JavaScript로 변경
    const periodSelect = document.querySelector('select#period');
    const detailOptions = document.querySelectorAll('input[name="detail-option"]');
    const storeSelect = document.querySelector('select#store');

    function onInputChange() {
        const selectedYValue = document.querySelector('input[name="detail-option"]:checked').value;
        const selectedStore = storeSelect.value;
        updateGraph(selectedYValue, selectedStore);
    }

    periodSelect.addEventListener('change', onInputChange);
    storeSelect.addEventListener('change', onInputChange);
    detailOptions.forEach(option => {
        option.addEventListener('change', onInputChange);
    });
});
