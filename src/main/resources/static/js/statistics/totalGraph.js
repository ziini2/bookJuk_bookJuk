let period = $("#period").val();

document.addEventListener('DOMContentLoaded', function () {
    const margin = {top: 20, right: 40, bottom: 30, left: 50},
        width = 1200 - margin.left - margin.right,
        height = 350 - margin.top - margin.bottom;

    const parseTime = d3.timeParse("%Y-%m-%d");

    const x = d3.scaleTime().range([0, width]);
    const y = d3.scaleLinear().range([height, 0]);

    const svg = d3.select(".total_short_cut_chart").append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", `translate(${margin.left},${margin.top})`);

    $.ajax({
        url: "/admin/period/total",
        type: "GET",
        data: {
            "period": period
        },
        success: function (result) {
            // 데이터 파싱 및 변환
            const data = result.map(d => ({
                date: parseTime(d.date),
                revenue: +d.revenue,
                point: +d.point,
                rental: +d.rental,
                delay: +d.delay,
                newUser: +d.newUser,
                totalUser: +d.totalUser
            }));

            // 그래프 업데이트 함수
            function updateGraph(selectedYValue) {
                const valueline = d3.line()
                    .x(d => x(d.date))
                    .y(d => y(d[selectedYValue]));

                // x축 도메인 설정 (날짜 범위)
                x.domain(d3.extent(data, d => d.date));

                // y축 도메인 설정 (0 ~ 최대값)
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

                // x축 업데이트 (날짜만 표시)
                const xAxis = d3.axisBottom(x)
                    .ticks(d3.timeDay.every(1)) // 1일 간격으로 표시
                    .tickFormat(d3.timeFormat("%Y-%m-%d")); // 날짜 포맷 설정

                svg.selectAll(".x-axis").remove();
                svg.append("g")
                    .attr("class", "x-axis axis")
                    .attr("transform", `translate(0,${height})`)
                    .transition()
                    .duration(1000)
                    .call(xAxis);

                // y축 업데이트
                const yAxis = d3.axisLeft(y)
                    .ticks(5); // 적절한 y축 간격 설정

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
        },
        error: function (error) {
            console.error("Error fetching data:", error);
        }
    });
});
