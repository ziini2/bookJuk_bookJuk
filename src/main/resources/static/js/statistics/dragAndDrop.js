document.addEventListener('DOMContentLoaded', function () {
    const dragElement = document.querySelector('.fixed-period-select');

    let offsetX = 0, offsetY = 0;
    let isDragging = false;

    // 마우스를 누를 때의 동작 정의
    dragElement.addEventListener('mousedown', function (e) {
        // 드래그가 필요하지 않은 요소에서 이벤트 발생 시 드래그 시작을 막음
        if (e.target.tagName === 'SELECT' || e.target.tagName === 'OPTION') {
            return;
        }

        // 기본 동작 방지
        e.preventDefault();

        // 드래그할 때의 초기 위치 저장
        const rect = dragElement.getBoundingClientRect();
        dragElement.style.left = `${rect.left}px`;
        dragElement.style.right = 'auto'; // right 속성을 제거해서 left를 사용하도록 설정

        offsetX = e.clientX - rect.left;
        offsetY = e.clientY - rect.top;

        // 드래그 중임을 표시
        isDragging = true;

        // 마우스 움직임에 대한 이벤트 핸들러 추가
        document.addEventListener('mousemove', moveElement);
        document.addEventListener('mouseup', stopDragging);
    });

    // 요소를 움직이는 함수 정의
    function moveElement(e) {
        if (!isDragging) return;

        // 드래그할 때 새로운 위치 계산
        const newX = e.clientX - offsetX;
        const newY = e.clientY - offsetY;

        // 새로운 위치 설정
        dragElement.style.top = `${newY}px`;
        dragElement.style.left = `${newX}px`;
    }

    // 드래그가 끝났을 때 이벤트 핸들러 제거
    function stopDragging() {
        if (!isDragging) return;

        // 마우스 이동과 관련된 이벤트 제거
        document.removeEventListener('mousemove', moveElement);
        document.removeEventListener('mouseup', stopDragging);

        // 드래그 중이 아님을 표시
        isDragging = false;
    }
});
