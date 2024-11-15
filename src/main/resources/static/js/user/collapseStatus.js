$(document).ready(function() {
  const rentCollapse = $("#rentInfo");
  const pointCollapse = $("#pointInfo");

  // 페이지 로드 시 localStorage에 저장된 상태로 설정
  if (localStorage.getItem("rentInfo") === "open") {
    rentCollapse.collapse("show");
  }
  if (localStorage.getItem("pointInfo") === "open") {
    pointCollapse.collapse("show");
  }

  // 콜랩스 상태 변경 시 localStorage에 저장
  rentCollapse.on("shown.bs.collapse", function() {
    localStorage.setItem("rentInfo", "open");
  });
  rentCollapse.on("hidden.bs.collapse", function() {
    localStorage.setItem("rentInfo", "closed");
  });

  pointCollapse.on("shown.bs.collapse", function() {
    localStorage.setItem("pointInfo", "open");
  });
  pointCollapse.on("hidden.bs.collapse", function() {
    localStorage.setItem("pointInfo", "closed");
  });
});