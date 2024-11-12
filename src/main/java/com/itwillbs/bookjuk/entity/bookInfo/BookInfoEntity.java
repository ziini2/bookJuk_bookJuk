package com.itwillbs.bookjuk.entity.bookInfo;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "book_info")
public class BookInfoEntity {

	// 도서번호
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookNum;

	// 도서명
	@Column(nullable = false)
	private String bookName;

	// 저자
	@Column(nullable = false)
	private String author;

	// 줄거리
	@Column(nullable = false)
	private String story;

	// 관심설정
	@Column(nullable = false)
	private int interest;

	// 출판사
	@Column(nullable = false)
	private String publish;

	// 장르ID
	@Column(nullable = false)
	private Long genreId;

	// ISBN번호 => String 으로 변경
	// ISBN이 0으로 시작하거나 특정 패턴을 가진 경우,
	// 문자열로 처리하는 것이 좋을것같다.
	@Column(nullable = false)
	private String isbn;

	// 출판일
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(nullable = false)
	private LocalDate publishDate;
	
	// 대여금액
	@Column(nullable = false)
	private Long rentMoney;


	
	 // 책 정보 엔티티 생성 메소드
    public static BookInfoEntity createBookInfoEntity(String bookName, String author, String story, int interest, 
                                                      String publish, Long genreId, String isbn, LocalDate publishDate) {
        return BookInfoEntity.builder()
                .bookName(bookName)
                .author(author)
                .story(story)
                .interest(interest)
                .publish(publish)
                .genreId(genreId)
                .isbn(isbn)
                .publishDate(publishDate)
                .build();
    }
    
}
