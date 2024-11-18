// 도서 정보를 가져와 수정 모달에 채우기
       function showBookDetails(bookId) {
           $.ajax({
               url: `/admin/books/${bookId}`,
               type: 'GET',
               success: function (data) {
                   $('#updateBookId').val(data.booksId);
                   $('#updateISBN').val(data.isbn);
                   $('#updateBookName').val(data.bookName);
                   $('#updateAuthor').val(data.author);
                   $('#updatePublish').val(data.publish);
                   $('#updatePublishDate').val(data.publishDate);
                   $('#updateBookStatus').val(data.bookStatus);
                   $('#updateRentMoney').val(data.rentMoney);

                   const modal = new bootstrap.Modal(document.getElementById('updateBookModal'));
                   modal.show();
               },
               error: function () {
                   alert('도서 정보를 가져오는 데 실패했습니다.');
               }
           });
       }

       // 수정된 도서 정보를 서버에 전송
       $('#saveChangesButton').on('click', function () {
           const formData = $('#updateBookForm').serialize();

           $.ajax({
               url: '/admin/books/update',
               type: 'POST',
               contentType: 'application/x-www-form-urlencoded',
               data: formData,
               success: function () {
                   alert('도서 정보가 성공적으로 수정되었습니다.');
                   $('#updateBookModal').modal('hide');
                   location.reload();
               },
               error: function () {
                   alert('도서 정보를 수정하는 데 실패했습니다.');
               }
           });
       });