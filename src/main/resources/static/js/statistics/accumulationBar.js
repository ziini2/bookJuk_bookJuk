document.addEventListener('DOMContentLoaded', function () {
    // 간단한 데이터를 정의합니다.
    const data = [
        { year: "해운대점", "대여료": 3000, "연장료": 1500, "연체료": 800 },
        { year: "남구점", "대여료": 3500, "연장료": 1300, "연체료": 700 },
        { year: "서면점", "대여료": 4000, "연장료": 1200, "연체료": 600 },
        { year: "광안리점", "대여료": 4500, "연장료": 1100, "연체료": 500 },
    ];

    // 색상 정의
    const colors = new Map([
        ["대여료", "#24693D"],
        ["연장료", "#EE7423"],
        ["연체료", "#7AAAD0"]
    ]);

    // 누적 데이터를 생성
    const series = d3.stack()
        .keys(colors.keys())
        .order(d3.stackOrderNone)
        .offset(d3.stackOffsetNone)(data);

    // SVG 및 차트 설정
    const width = 400; // 기존 width보다 작게 설정하여 범례와 같이 표시되도록 함
    const height = 300;
    const margin = { top: 20, right: 20, bottom: 50, left: 50 };

    const svg = d3.select(".accumulation-bar-chart")
        .append("svg")
        .attr("viewBox", `0 0 ${width} ${height}`)
        .attr("width", width)
        .attr("height", height);

    // x축과 y축 범위 설정
    const x = d3.scaleBand()
        .domain(data.map(d => d.year))
        .range([margin.left, width - margin.right])
        .padding(0.1);

    const y = d3.scaleLinear()
        .domain([0, d3.max(series, s => d3.max(s, d => d[1]))]).nice()
        .range([height - margin.bottom, margin.top]);

    const color = d3.scaleOrdinal()
        .domain(colors.keys())
        .range(colors.values());

    // x축 추가
    svg.append("g")
        .attr("transform", `translate(0,${height - margin.bottom})`)
        .call(d3.axisBottom(x).tickSizeOuter(0));

    // y축 추가
    svg.append("g")
        .attr("transform", `translate(${margin.left},0)`)
        .call(d3.axisLeft(y).ticks(null, "s"))
        .call(g => g.select(".domain").remove());

    // 누적 막대 그래프 추가
    svg.append("g")
        .selectAll("g")
        .data(series)
        .join("g")
        .attr("fill", ({ key }) => color(key))
        .selectAll("rect")
        .data(d => d)
        .join("rect")
        .attr("x", d => x(d.data.year))
        .attr("y", d => y(d[1]))
        .attr("height", d => y(d[0]) - y(d[1]))
        .attr("width", x.bandwidth())
        .append("title")
        .text(d => `${d.data.year}: ${d3.format(",")(d[1] - d[0])}`);

    // 범례 추가
    const legendContainer = d3.select(".bar-chart-legend");
    legendContainer.selectAll(".bar-chart-legend-item")
        .data(colors.keys())
        .join("div")
        .attr("class", "bar-chart-legend-item")
        .call(g => {
            g.append("div")
                .attr("class", "bar-chart-legend-color")
                .style("background-color", d => color(d));
            g.append("span")
                .text(d => d);
        });
});