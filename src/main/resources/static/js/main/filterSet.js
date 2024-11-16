function setGenre(element){
  let textGenre = $(element).text();
  $("#genreHidden").val(textGenre);
  $("#genreButton").text(textGenre);
}
function setStoreName(element){
  let textStoreName = $(element).text();
$("#storeHidden").val(textStoreName);
$("#storeButton").text(textStoreName);
}