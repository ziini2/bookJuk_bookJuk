function downloadExcel(jsonData)
{
    // 워크북 생성
    const worksheet = XLSX.utils.json_to_sheet(jsonData); // JSON -> Sheet 변환
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "Sheet1");

    // 워크북을 엑셀 파일로 작성
    const excelBuffer = XLSX.write(workbook, {bookType: "xlsx", type: "array"});

    // Blob 생성
    const blob = new Blob([excelBuffer], {type: "application/octet-stream"});

    // 다운로드 트리거
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = "data.xlsx";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}
;

// 전역으로 등록
window.downloadExcel = downloadExcel;