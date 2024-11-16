function bookCartAdd (){
  const pk = parseInt($(".bookCartAdd").attr("data-pk"));
  console.log(pk);

  $.ajax({
    type: "POST",
    url: "/user/bookCart/add",
    data: { booksId: pk },
    success: (res) => {
      console.log(res)
      if (res.response === "success"){
        alert("장바구니 담기 성공 하였습니다.")
        $("#bookInfoModal").modal('hide');
      }
      else
        alert("장바구니 담기 실패 하였습니다.")
    },
    error: (err) => {
      console.log(err);
    }
  })



}