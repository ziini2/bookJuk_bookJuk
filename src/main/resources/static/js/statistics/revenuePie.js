document.addEventListener('DOMContentLoaded', function () {
    // 데이터 정의
    const data = [
        { "대여료": 53245, "연체료": 200 },
        { "대여료": 28479, "연체료": 200 },
        { "대여료": 19697, "연체료": 200 },
        { "대여료": 24037, "연체료": 200 },
        { "대여료": 40245, "연체료": 200 }
    ];

    // 차트 함수 정의 및 호출
    const chart = (data) => {
        const width = 400;
        const height = Math.min(500, width / 2);
        const outerRadius = height / 2 - 10;
        const innerRadius = outerRadius * 0.75;
        const color = d3.scaleOrdinal(d3.schemeCategory10);

        // SVG 요소 생성
        const svg = d3.select(".revenue-pie")
            .append("svg")
            .attr("viewBox", [-width / 2, -height / 2, width, height])
            .attr("width", width)
            .attr("height", height);

        // Arc 설정
        const arc = d3.arc()
            .innerRadius(innerRadius)
            .outerRadius(outerRadius);

        // Pie 설정
        const pie = d3.pie()
            .sort(null)
            .value(d => d["대여료"]);

        // Path 요소 생성
        const path = svg.selectAll("path")
            .data(pie(data))
            .join("path")
            .attr("fill", (d, i) => color(i))
            .attr("d", arc)
            .each(function (d) { this._current = d; }); // 초기 각도 저장

        // 값 변경을 위한 함수
        function change(value) {
            pie.value(d => d[value]); // 새로운 값 설정
            const newData = pie(data); // 새로운 데이터로 갱신

            path.data(newData) // 데이터 바인딩
                .transition().duration(750)
                .attrTween("d", arcTween); // arc 트윈을 사용해 애니메이션 설정
        }

        // arcTween 함수 정의
        function arcTween(a) {
            const i = d3.interpolate(this._current, a);
            this._current = i(1); // 현재 각도를 업데이트
            return t => arc(i(t));
        }

        // 이벤트 리스너 추가
        d3.selectAll("input[name='sale-option']").on("change", function () {
            change(this.value); // 사용자가 선택한 값으로 차트를 업데이트
        });

        // 범례 추가
        const legendContainer = d3.select(".legend");
        const legendItems = legendContainer.selectAll(".legend-item")
            .data(data)
            .join("div")
            .attr("class", "legend-item");

        legendItems.append("div")
            .attr("class", "legend-color-box")
            .style("background-color", (d, i) => color(i));

        legendItems.append("span")
            .text((d, i) => `지점 ${i + 1}`);

        return Object.assign(svg.node(), { change });
    };

    // 차트 생성 호출
    chart(data);
});