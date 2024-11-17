document.addEventListener('DOMContentLoaded', function () {

    let period;
    let salesOption;

    $('#period').on('change', function () {
        period = $(this).val();
        window.globalState.requestData(); // 전역 requestData 함수 호출
    });

    $('.sales-option').on('click', function () {
        salesOption = $(this).text();
        $('.sales-btn').text(`매출항목: ${salesOption}`);
        $('.sales-option').removeClass('active');
        $(this).addClass('active');
        window.globalState.requestData(); // 전역 requestData 함수 호출
    });

    const requestData = function () {
        const storeList = Array.from(window.globalState.revenueStoreList); // 포인트 분석용 storeList 참조
        $('.point-bar').empty();
        $.ajax({
            url: '/admin/period/point',
            method: 'GET',
            data: {
                period: period,
                storeList: storeList,
                salesOption: salesOption
            },
            success: function (data) {
                // D3.js 차트 렌더링
                const width = 400;
                const height = 300;
                const margin = {top: 20, right: 30, bottom: 50, left: 50};

                const svg = d3.select(".point-bar")
                    .append("svg")
                    .attr("width", width)
                    .attr("height", height)
                    .attr("viewBox", `0 0 ${width} ${height}`);

                const x = d3.scaleBand().range([margin.left, width - margin.right]).padding(0.1);
                const y = d3.scaleLinear().range([height - margin.bottom, margin.top]);

                const color = d3.scaleOrdinal().range(["#1f77b4", "#ff7f0e"]);

                const stack = d3.stack().keys(["rentalFee", "overdueFee"]);

                function updateChart(dataset) {
                    const stackedData = stack(dataset);

                    x.domain(dataset.map(d => d.category));
                    y.domain([0, d3.max(stackedData[stackedData.length - 1], d => d[1])]);

                    svg.selectAll(".x-axis").remove();
                    svg.selectAll(".y-axis").remove();

                    svg.append("g")
                        .attr("class", "x-axis")
                        .attr("transform", `translate(0,${height - margin.bottom})`)
                        .call(d3.axisBottom(x));

                    svg.append("g")
                        .attr("class", "y-axis")
                        .attr("transform", `translate(${margin.left},0)`)
                        .call(d3.axisLeft(y));

                    const groups = svg.selectAll(".layer")
                        .data(stackedData, d => d.key);

                    groups.enter()
                        .append("g")
                        .attr("class", "layer")
                        .attr("fill", (d, i) => color(i))
                        .merge(groups)
                        .selectAll("rect")
                        .data(d => d)
                        .join("rect")
                        .attr("x", d => x(d.data.category))
                        .attr("y", d => y(d[1]))
                        .attr("height", d => y(d[0]) - y(d[1]))
                        .attr("width", x.bandwidth());

                    groups.exit().remove();

                    const legend = d3.select("#point-bar-legend");
                    legend.selectAll(".legend-item").remove();

                    ["rentalFee", "overdueFee"].forEach((key, i) => {
                        const item = legend.append("div").attr("class", "legend-item");
                        item.append("div").attr("class", "legend-color-box")
                            .style("background-color", color(i));
                        item.append("span").text(key === "rentalFee" ? "대여료" : "연체료");
                    });
                }

                d3.selectAll("input[name='point-option']").on("change", function () {
                    const selectedOption = this.value;
                    updateChart(data[selectedOption]);
                });

                updateChart(data.gender); // 초기 렌더링
            }
        });
    };

    // requestData를 전역 객체로 설정
    window.globalState.requestData = requestData;

    requestData(); // 초기 요청
});
