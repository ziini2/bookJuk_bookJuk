package com.itwillbs.bookjuk.dto;

import java.sql.Timestamp;
import java.time.LocalDate;

import com.itwillbs.bookjuk.domain.books.BookStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

	private String isbn; // ISBN 번호
	//isbn 으로 불러온 값들
	private String bookName; // 도서 이름
	private String author; // 저자
	private String publish; // 출판사
	private LocalDate publishDate; // 출판일
	private String story; // 책 소개
	private Long genreId; //장르ID
	
	//직접 입력해서 저장할 값들
	private Long storeCode; //지점코드
	private BookStatus bookStatus; // 도서상태
	private Timestamp bookDate; //입고일
	private Long rentMoney; //대여금액
	private Long rentCount; //대여횟수
	
	
}
