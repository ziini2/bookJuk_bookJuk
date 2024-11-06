document.addEventListener('DOMContentLoaded', function () {
    // 초기 데이터 정의
    const data = {
        gender: [
            { category: "남", count: 500 },
            { category: "여", count: 600 }
        ],
        age: [
            { category: "10대", count: 100 },
            { category: "20대", count: 250 },
            { category: "30대", count: 300 },
            { category: "40대", count: 200 },
            { category: "50대", count: 150 },
            { category: "60대↑", count: 100 }
        ],
        genre: [
            { category: "소설", count: 150 },
            { category: "자기계발", count: 120 },
            { category: "역사", count: 100 },
            { category: "과학", count: 80 },
            { category: "예술", count: 50 },
            { category: "여행", count: 60 }
        ]
    };

    // 차트 설정
    const width = 400;
    const height = 260;
    const margin = { top: 20, right: 30, bottom: 40, left: 40 };

    // SVG 생성
    const svg = d3.select(".customer-bar")
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
            .attr("y", d => y(d.count))
            .attr("height", d => y(0) - y(d.count))
            .attr("width", x.bandwidth())
            .attr("fill", (d, i) => color(i));

        // 제거된 막대
        bars.exit().remove();

        // 범례 업데이트
        const legendContainer = d3.select("#cus-bar-legend");
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
    d3.selectAll("input[name='customer-option']").on("change", function () {
        const selectedOption = this.value;
        updateAxis(data[selectedOption]);
        updateChart(data[selectedOption]);
    });

    // 초기 차트 렌더링 호출
    renderInitialChart();
});