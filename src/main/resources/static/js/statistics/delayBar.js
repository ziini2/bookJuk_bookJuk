document.addEventListener('DOMContentLoaded', function () {

    let period;

    $('#period').on('change', function () {
        period = $(this).val();
        window.globalState.delayRequestData(); // 전역 requestData 함수 호출
    });

    const delayRequestData = function () {
        const storeList = Array.from(window.globalState.delayStoreList); // 연체 분석용 storeList 참조
        $(".delay-bar").empty();
        $.ajax({
            url: '/admin/period/delay',
            method: 'GET',
            data: {
                period: period,
                storeList: storeList
            },
            success: function (data) {
                // 차트 설정
                const width = 400;
                const height = 260;
                const margin = { top: 20, right: 30, bottom: 40, left: 40 };

                // SVG 생성
                const svg = d3.select(".delay-bar")
                    .append("svg")
                    .attr("width", width)
                    .attr("height", height)
                    .attr("viewBox", `0 0 ${width} ${height}`);

                // x축과 y축 범위 설정
                const x = d3.scaleBand()
                    .range([margin.left, width - margin.right])
                    .padding(0.1);

                const y = d3.scaleLinear()
                    .range([height - margin.bottom, margin.top]);

                const color = d3.scaleOrdinal(d3.schemeCategory10);

                // 축 설정 함수
                function updateAxis(data) {
                    x.domain(data.map(d => d.category));
                    y.domain([0, d3.max(data, d => d.count)]);

                    // x축 추가
                    svg.selectAll(".x-axis").remove();
                    svg.append("g")
                        .attr("class", "x-axis")
                        .attr("transform", `translate(0,${height - margin.bottom})`)
                        .call(d3.axisBottom(x).tickSizeOuter(0));

                    // y축 추가
                    svg.selectAll(".y-axis").remove();
                    svg.append("g")
                        .attr("class", "y-axis")
                        .attr("transform", `translate(${margin.left},0)`)
                        .call(d3.axisLeft(y));
                }

                // 차트 업데이트 함수
                function updateChart(dataset) {
                    // 데이터 바인딩 및 rect 업데이트
                    const bars = svg.selectAll(".bar")
                        .data(dataset, d => d.category);

                    // 기존 막대 업데이트
                    bars
                        .attr("x", d => x(d.category))
                        .attr("y", d => y(d.count))
                        .attr("height", d => y(0) - y(d.count))
                        .attr("width", x.bandwidth());

                    // 새로운 막대 추가
                    bars.enter()
                        .append("rect")
                        .attr("class", "bar")
                        .attr("x", d => x(d.category))
                        .attr("y", y(0)) // 초기 y 위치를 차트의 맨 아래로 설정
                        .attr("height", 0) // 초기 높이를 0으로 설정
                        .attr("width", x.bandwidth())
                        .attr("fill", (d, i) => color(i))
                        .transition() // 추가된 막대에 애니메이션 적용
                        .duration(1000)
                        .attr("y", d => y(d.count)) // 목표 y 위치로 이동
                        .attr("height", d => y(0) - y(d.count)); // 최종 높이로 설정

                    // 제거된 막대
                    bars.exit().remove();

                    // 범례 업데이트
                    const legendContainer = d3.select("#delay-bar-legend");
                    legendContainer.selectAll(".legend-item").remove();

                    const legendItems = legendContainer.selectAll(".legend-item")
                        .data(dataset)
                        .enter()
                        .append("div")
                        .attr("class", "legend-item");

                    legendItems.append("div")
                        .attr("class", "legend-color-box")
                        .style("background-color", (d, i) => color(i));

                    legendItems.append("span")
                        .text(d => d.category);
                }

                // 초기 차트 렌더링
                function renderInitialChart() {
                    const initialData = data.gender; // 초기 데이터: 성별
                    updateAxis(initialData);
                    updateChart(initialData);
                }

                // 라디오 버튼 이벤트 리스너
                d3.selectAll("input[name='delay-option']").on("change", function () {
                    const selectedOption = this.value;
                    updateAxis(data[selectedOption]);
                    updateChart(data[selectedOption]);
                });

                // 초기 차트 렌더링 호출
                renderInitialChart();
            }
        })
    }

    // delayRequestData를 전역 객체로 설정
    window.globalState.delayRequestData = delayRequestData;

    delayRequestData();


});