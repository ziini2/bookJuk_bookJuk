package com.itwillbs.bookjuk.entity.bookInfo;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Entity
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
	
	// 출판사
	@Column(nullable = false)
	private String publish;
	
	// 책소개
	@Column(nullable = false)
	private String story;

//	// 관심설정
//	@Column(nullable = false)
//	private int interest;

	// 장르ID (GenreEntity와 연결)
	@OneToMany
	@JoinColumn(name = "genreID", referencedColumnName = "genreID", nullable = false) // 외래 키 설정
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

	// 입고일
	@CreationTimestamp
	@Column(name = "book_date", nullable = true, updatable = false)
	private LocalDateTime bookDate;

	//엔티티가 처음 저장되기 전에 bookDate가 null일 경우 ,
	//현재 시간으로 설정할 수 있도록 한다.
	  @PrePersist
	    public void prePersist() {
	        if (this.bookDate == null) {
	            this.bookDate = LocalDateTime.now();
	        }
	    }
	
	public Long getBookNum() {
		return bookNum;
	}

	public void setBookNum(Long bookNum) {
		this.bookNum = bookNum;
	}

	public LocalDate getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(LocalDate publishDate) {
		this.publishDate = publishDate;
	}

}
