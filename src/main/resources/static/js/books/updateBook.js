function showBookDetails(row) {
        // 선택된 행의 데이터를 가져오기
        const bookId = row.querySelector('[data-book-id]').textContent;
        const isbn = row.querySelector('[data-isbn]').textContent;
        const bookName = row.querySelector('[data-book-name]').textContent;
        const author = row.querySelector('[data-author]').textContent;
        const publish = row.querySelector('[data-publish]').textContent;
        const publishDate = row.querySelector('[data-publish-date]').textContent;
        const bookDate = row.querySelector('[data-book-date]').textContent;
        const rentStatus = row.querySelector('[data-rent-status]').textContent;
        const bookStatus = row.querySelector('[data-book-status]').textContent;
        const rentMoney = row.querySelector('[data-rent-money]').textContent;

        // 모달에 데이터 삽입
        document.getElementById('modalBookId').textContent = bookId;
        document.getElementById('modalISBN').textContent = isbn;
        document.getElementById('modalBookName').textContent = bookName;
        document.getElementById('modalAuthor').textContent = author;
        document.getElementById('modalPublish').textContent = publish;
        document.getElementById('modalPublishDate').textContent = publishDate;
        document.getElementById('modalBookDate').textContent = bookDate;
        document.getElementById('modalRentStatus').textContent = rentStatus;
        document.getElementById('modalBookStatus').textContent = bookStatus;
        document.getElementById('modalRentMoney').textContent = rentMoney;

        // 모달 표시
        const modal = new bootstrap.Modal(document.getElementById('bookModal'));
        modal.show();
      }
