document.addEventListener('DOMContentLoaded', function () {

    let period = $('#period').val();
    let salesOption = $('.sales-option.active').text();

    // 초기 selectedOption 설정
    let selectedOption = $("input[name='point-option']:checked").val() || "rentalFee";

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
                // 상위 5개 지점만 필터링
                if (data[selectedOption]) {
                    let filteredData = data[selectedOption]
                    if (selectedOption === "store") {

                        filteredData = filteredData.sort((a, b) => (b.rentalFee + b.overdueFee) - (a.rentalFee + a.overdueFee)) // 총 매출을 기준으로 정렬
                            .slice(0, 5); // 상위 5개 항목 선택
                    }
                    updateChart(filteredData);


                }  else {
                    console.error("데이터 형식이 올바르지 않습니다.", data);
                }

                // 옵션이 변경되었을 때 차트를 업데이트하도록 설정
                d3.selectAll("input[name='point-option']").on("change", function () {
                    selectedOption = this.value;
                    if (data[selectedOption]) {
                        let filteredData = data[selectedOption]
                        if (selectedOption === "store") {
                            filteredData = filteredData.sort((a, b) => (b.rentalFee + b.overdueFee) - (a.rentalFee + a.overdueFee)) // 총 매출을 기준으로 정렬
                                .slice(0, 5); // 상위 5개 항목 선택

                        }
                        updateChart(filteredData);
                    } else {
                        console.error("선택된 데이터가 없습니다.", selectedOption);
                    }
                });

                // D3.js 차트 렌더링 함수
                function updateChart(dataset) {
                    $('.point-bar').empty();
                    const width = 400;
                    const height = 300;
                    const margin = {top: 20, right: 30, bottom: 50, left: 50};

                    const svg = d3.select(".point-bar")
                        .append("svg")
                        .attr("width", width)
                        .attr("height", height)
                        .attr("viewBox", `0 0 ${width} ${height}`);

                    const x = d3.scaleBand()
                        .domain(dataset.map(d => d.category))
                        .range([margin.left, width - margin.right])
                        .padding(0.3); // 패딩을 늘려 막대 간의 간격 확보

                    const y = d3.scaleLinear()
                        .domain([0, d3.max(dataset, d => d.rentalFee + d.overdueFee)]) // 최대값 설정
                        .nice()
                        .range([height - margin.bottom, margin.top]);

                    const color = d3.scaleOrdinal().range(["#1f77b4", "#ff7f0e"]);

                    const stack = d3.stack().keys(["rentalFee", "overdueFee"]);

                    const stackedData = stack(dataset);

                    // 기존 x-axis와 y-axis를 제거하고, 새로운 축을 추가합니다.
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

                    // 데이터를 레이어에 결합합니다.
                    const groups = svg.selectAll(".layer")
                        .data(stackedData);

                    // 레이어 그룹을 생성합니다.
                    const groupsEnter = groups.enter()
                        .append("g")
                        .attr("class", "layer")
                        .attr("fill", (d, i) => color(i));

                    // 기존 레이어 그룹과 새 레이어 그룹을 병합합니다.
                    const mergedGroups = groupsEnter.merge(groups);

                    // 막대의 초기 위치를 설정하여 애니메이션이 위로 올라오도록 처리합니다.
                    mergedGroups.selectAll("rect")
                        .data(d => d)
                        .join(
                            enter => enter.append("rect")
                                .attr("x", d => x(d.data.category))
                                .attr("y", y(0)) // 막대가 아래에서 시작
                                .attr("height", 0) // 초기 높이를 0으로 설정
                                .attr("width", x.bandwidth())
                                .call(enter => enter.transition() // 위로 올라오면서 애니메이션
                                    .duration(750)
                                    .attr("y", d => y(d[1]))
                                    .attr("height", d => y(d[0]) - y(d[1]))),
                            update => update.call(update => update.transition() // 기존 막대를 부드럽게 전환
                                .duration(750)
                                .attr("y", d => y(d[1]))
                                .attr("height", d => y(d[0]) - y(d[1])))
                        );

                    // 퇴장하는 막대에 대한 애니메이션
                    groups.exit().selectAll("rect")
                        .transition()
                        .duration(750)
                        .attr("y", y(0))
                        .attr("height", 0)
                        .remove();

                    groups.exit().remove();

                    // 범례 업데이트
                    const legend = d3.select("#point-bar-legend");
                    legend.selectAll(".legend-item").remove();

                    ["rentalFee", "overdueFee"].forEach((key, i) => {
                        const item = legend.append("div").attr("class", "legend-item");
                        item.append("div").attr("class", "legend-color-box")
                            .style("background-color", color(i));
                        item.append("span").text(key === "rentalFee" ? "대여료" : "연체료");
                    });
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Ajax 요청 오류:", textStatus, errorThrown);
            }
        });
    };

    // requestData를 전역 객체로 설정
    window.globalState.requestData = requestData;

    requestData(); // 초기 요청
});
