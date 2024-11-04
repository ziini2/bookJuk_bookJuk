package com.itwillbs.bookjuk.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
	
	// 재고
	@Column(nullable = false)
	private Long inventory;
	
	
	
	
}
