document.addEventListener('DOMContentLoaded', function () {

    let period = $("#period").val();

    const margin = {top: 20, right: 40, bottom: 30, left: 50},
        width = 1200 - margin.left - margin.right,
        height = 350 - margin.top - margin.bottom;

    const parseTime = d3.timeParse("%Y-%m-%d");

    const x = d3.scaleBand().range([0, width]).padding(0.1);
    const y = d3.scaleLinear().range([height, 0]);

    const svg = d3.select(".total_short_cut_chart").append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", `translate(${margin.left},${margin.top})`);

    let selectedYValue = 'revenue'; // 초기 선택 값
    rebuildGraph();

    function rebuildGraph() {
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
                function updateGraph() {
                    // x축 도메인 설정 (날짜 범위)
                    x.domain(data.map(d => d.date));

                    // y축 도메인 설정 (0 ~ 최대값)
                    const yMax = d3.max(data, d => d[selectedYValue]);
                    y.domain([0, yMax > 0 ? Math.ceil(yMax * 1.1) : 1]); // 모든 값이 0일 때 최소값을 1로 설정

                    // 막대 업데이트
                    const bars = svg.selectAll(".bar")
                        .data(data);

                    bars.enter()
                        .append("rect")
                        .attr("class", "bar")
                        .attr("x", d => x(d.date))
                        .attr("width", x.bandwidth())
                        .attr("y", height) // 시작 위치를 아래로 설정
                        .attr("height", 0) // 시작 높이를 0으로 설정
                        .style("fill", "steelblue") // 막대 색상 설정
                        .merge(bars)
                        .transition()
                        .duration(1000)
                        .attr("y", d => y(d[selectedYValue]))
                        .attr("height", d => height - y(d[selectedYValue]));

                    bars.exit()
                        .transition()
                        .duration(500)
                        .attr("y", height) // 아래로 내려가는 애니메이션
                        .attr("height", 0)
                        .remove();

                    // x축 업데이트
                    let tickValues = [];
                    if (period === "week") {
                        tickValues = data.map(d => d.date); // 1일 간격 모든 날짜 표시
                    } else if (period === "month" || period === "quarter") {
                        tickValues = data.filter((d, i) => i % 7 === 0).map(d => d.date); // 7일 간격 표시
                    }

                    const xAxis = d3.axisBottom(x)
                        .tickValues(tickValues)
                        .tickFormat(d3.timeFormat("%Y-%m-%d"));

                    svg.selectAll(".x-axis").remove();
                    svg.append("g")
                        .attr("class", "x-axis axis")
                        .attr("transform", `translate(0,${height})`)
                        .transition()
                        .duration(1000)
                        .call(xAxis);

                    // y축 업데이트
                    const yAxis = d3.axisLeft(y)
                        .ticks(5) // 적절한 y축 간격 설정
                        .tickFormat(d3.format("d")); // 정수 값만 표시

                    svg.selectAll(".y-axis").remove();
                    svg.append("g")
                        .attr("class", "y-axis axis")
                        .transition()
                        .duration(1000)
                        .call(yAxis);
                }

                // 초기 그래프 표시
                updateGraph();

                // 라디오 버튼 이벤트 리스너
                const detailOptions = document.querySelectorAll('input[name="detail-option"]');

                function onInputChange() {
                    selectedYValue = document.querySelector('input[name="detail-option"]:checked').value;
                    updateGraph();
                }

                detailOptions.forEach(option => {
                    option.addEventListener('change', onInputChange);
                });
            },
            error: function (error) {
                console.error("Error fetching data:", error);
            }
        });
    }

    // period 값이 변경될 때마다 그래프 재생성
    $(document).ready(function () {
        $("#period").change(function () {
            period = $("#period").val(); // 값을 가져옴
            rebuildGraph(); // 그래프 재생성 함수 호출
        });
    });

});
