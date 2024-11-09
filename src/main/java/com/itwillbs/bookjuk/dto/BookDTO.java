package com.itwillbs.bookjuk.dto;



import java.sql.Timestamp;

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
	
	//책번호
	private Long bookNum;
	
	//isbn
	private Long ISBN;
	
	//책이름
	private String bookName;
	
	//저자
	private String author;
	
	//출판사
	private String publish;
	
	//출판일
	private Timestamp publishDate;
	
	//입고일
	private Timestamp bookDate;
	
	//재고
	private Long inventory;
	
	//줄거리
	private String story;
	
	//도서상태(enum 클래스의 정의된 것만 사용)
	private BookStatus bookStatus;
	
	//대여금액
	private Long rentMoney;
	
	//지점ID
	private Long storeCode; 
	
	//장르ID
	private Long genreId;

}
