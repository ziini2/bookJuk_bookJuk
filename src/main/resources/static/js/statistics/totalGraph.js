let period = $("#period").val();

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

    let data = [];
    $.ajax({
        url: "/admin/statistics/total",
        type: "GET",
        data: {
            "period": period
        },
        async: false,
        success: function (result) {
            data = result;
        }
    });

    // let data = [
    //     {date: "1-May-12", revenue: 58.13, rental: 40, newUser: 20, totalUser: 200, point: 150, delay: 2},
    //     {date: "30-Apr-12", revenue: 53.98, rental: 45, newUser: 30, totalUser: 230, point: 200, delay: 3},
    //     {date: "27-Apr-12", revenue: 67.00, rental: 50, newUser: 40, totalUser: 270, point: 180, delay: 1},
    //     {date: "26-Apr-12", revenue: 89.70, rental: 55, newUser: 35, totalUser: 305, point: 220, delay: 4},
    //     {date: "25-Apr-12", revenue: 99.00, rental: 60, newUser: 50, totalUser: 355, point: 240, delay: 2},
    //     {date: "24-Apr-12", revenue: 130.28, rental: 65, newUser: 55, totalUser: 410, point: 260, delay: 3},
    //     {date: "23-Apr-12", revenue: 166.70, rental: 70, newUser: 60, totalUser: 470, point: 300, delay: 5},
    //     {date: "22-Apr-12", revenue: 120.70, rental: 75, newUser: 45, totalUser: 515, point: 280, delay: 2},
    //     {date: "21-Apr-12", revenue: 98.10, rental: 80, newUser: 70, totalUser: 585, point: 310, delay: 4},
    //     {date: "20-Apr-12", revenue: 234.98, rental: 90, newUser: 75, totalUser: 660, point: 350, delay: 3},
    //     {date: "19-Apr-12", revenue: 345.44, rental: 100, newUser: 80, totalUser: 740, point: 400, delay: 6},
    //     {date: "18-Apr-12", revenue: 443.34, rental: 110, newUser: 85, totalUser: 825, point: 420, delay: 3},
    //     {date: "17-Apr-12", revenue: 543.70, rental: 120, newUser: 90, totalUser: 915, point: 450, delay: 5},
    //     {date: "16-Apr-12", revenue: 580.13, rental: 125, newUser: 95, totalUser: 1010, point: 470, delay: 4},
    //     {date: "15-Apr-12", revenue: 510.13, rental: 100, newUser: 85, totalUser: 1095, point: 390, delay: 2},
    //     {date: "14-Apr-12", revenue: 490.20, rental: 110, newUser: 70, totalUser: 1165, point: 410, delay: 6},
    //     {date: "13-Apr-12", revenue: 605.23, rental: 130, newUser: 75, totalUser: 1240, point: 430, delay: 3},
    //     {date: "12-Apr-12", revenue: 622.77, rental: 140, newUser: 85, totalUser: 1325, point: 460, delay: 5},
    //     {date: "11-Apr-12", revenue: 626.20, rental: 145, newUser: 90, totalUser: 1415, point: 480, delay: 4},
    //     {date: "10-Apr-12", revenue: 628.44, rental: 150, newUser: 95, totalUser: 1510, point: 490, delay: 3},
    //     {date: "9-Apr-12", revenue: 636.23, rental: 155, newUser: 100, totalUser: 1610, point: 500, delay: 5},
    //     {date: "8-Apr-12", revenue: 640.30, rental: 160, newUser: 105, totalUser: 1715, point: 520, delay: 2},
    //     {date: "7-Apr-12", revenue: 635.20, rental: 158, newUser: 102, totalUser: 1817, point: 510, delay: 4},
    //     {date: "6-Apr-12", revenue: 610.45, rental: 140, newUser: 85, totalUser: 1902, point: 480, delay: 3},
    //     {date: "5-Apr-12", revenue: 633.68, rental: 162, newUser: 108, totalUser: 2010, point: 530, delay: 6},
    //     {date: "4-Apr-12", revenue: 624.31, rental: 155, newUser: 95, totalUser: 2105, point: 490, delay: 3},
    //     {date: "3-Apr-12", revenue: 629.32, rental: 150, newUser: 92, totalUser: 2197, point: 500, delay: 4},
    //     {date: "2-Apr-12", revenue: 618.63, rental: 140, newUser: 90, totalUser: 2287, point: 470, delay: 2},
    //     {date: "1-Apr-12", revenue: 620.90, rental: 145, newUser: 88, totalUser: 2375, point: 480, delay: 5},
    //     {date: "31-Mar-12", revenue: 599.55, rental: 135, newUser: 83, totalUser: 2458, point: 460, delay: 3},
    //     {date: "30-Mar-12", revenue: 609.86, rental: 138, newUser: 85, totalUser: 2543, point: 470, delay: 4},
    //     {date: "29-Mar-12", revenue: 617.62, rental: 142, newUser: 89, totalUser: 2632, point: 480, delay: 6},
    //     {date: "28-Mar-12", revenue: 614.48, rental: 140, newUser: 87, totalUser: 2719, point: 460, delay: 2},
    //     {date: "27-Mar-12", revenue: 606.98, rental: 135, newUser: 84, totalUser: 2803, point: 450, delay: 3},
    //     {date: "26-Mar-12", revenue: 620.34, rental: 145, newUser: 92, totalUser: 2895, point: 470, delay: 5},
    //     {date: "25-Mar-12", revenue: 635.22, rental: 150, newUser: 95, totalUser: 2990, point: 490, delay: 4},
    //     {date: "24-Mar-12", revenue: 645.45, rental: 155, newUser: 97, totalUser: 3087, point: 500, delay: 2},
    //     {date: "23-Mar-12", revenue: 652.60, rental: 160, newUser: 100, totalUser: 3187, point: 520, delay: 3},
    //     {date: "22-Mar-12", revenue: 660.22, rental: 165, newUser: 103, totalUser: 3290, point: 530, delay: 4},
    //     {date: "21-Mar-12", revenue: 670.80, rental: 170, newUser: 110, totalUser: 3400, point: 550, delay: 5},
    //     {date: "20-Mar-12", revenue: 680.50, rental: 175, newUser: 112, totalUser: 3512, point: 560, delay: 6}
    // ];


    // 데이터 파싱 및 변환
    data.forEach(d => {
        d.date = parseTime(d.date);
        d.revenue = +d.revenue;
        d.rental = +d.rental;
        d.newUser = +d.newUser;
        d.totalUser = +d.totalUser;
    });

    // 그래프 업데이트 함수
    function updateGraph(selectedYValue) {
        const valueline = d3.line()
            .x(d => x(d.date))
            .y(d => y(d[selectedYValue]));

        // x, y 축 도메인 설정
        x.domain(d3.extent(data, d => d.date));
        y.domain([0, d3.max(data, d => d[selectedYValue]) * 1.1]);

        // 선 업데이트
        const line = svg.selectAll(".line")
            .data([data]);

        line.enter()
            .append("path")
            .attr("class", "line")
            .merge(line)
            .transition()
            .duration(1000)
            .attr("d", valueline);

        line.exit().remove();

        // x 축 업데이트
        const xAxis = d3.axisBottom(x);
        svg.selectAll(".x-axis").remove();
        svg.append("g")
            .attr("class", "x-axis axis")
            .attr("transform", `translate(0,${height})`)
            .transition()
            .duration(1000)
            .call(xAxis);

        // y 축 업데이트
        const yAxis = d3.axisLeft(y);
        svg.selectAll(".y-axis").remove();
        svg.append("g")
            .attr("class", "y-axis axis")
            .transition()
            .duration(1000)
            .call(yAxis);
    }

    // 초기 그래프 표시
    updateGraph('revenue');

    // 라디오 버튼 이벤트 리스너
    const detailOptions = document.querySelectorAll('input[name="detail-option"]');

    function onInputChange() {
        const selectedYValue = document.querySelector('input[name="detail-option"]:checked').value;
        updateGraph(selectedYValue);
    }

    detailOptions.forEach(option => {
        option.addEventListener('change', onInputChange);
    });
});
