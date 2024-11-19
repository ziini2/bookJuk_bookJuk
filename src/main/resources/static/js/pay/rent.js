function rentBooks(){
  console.log("Ajax 호출 시작"); // Ajax 호출 전 디버깅
  $.ajax({
    type: "POST",
    url: "/user/rentDetail",
    success: (res) => {
      if (res.response === "success") {
		fetchUnreadNotifications();
        alert("대여 성공");
        location.href="/";
      } else {
        alert("대여 실패");
      }
    },
    error: (err) => {
      console.error("Ajax 요청 오류:", err);
    }
  });
}

function fetchUnreadNotifications() {
    $.ajax({
        url: `/user/notifications`,
        type: 'GET',
        success: function (count) {
			if (count > 0) {
				localStorage.setItem('unreadCount', count);
	            $('#notification-count').text(count);
	        }
        },
        error: function () {
            console.error('Failed to fetch unread notifications');
        }
    });
}


