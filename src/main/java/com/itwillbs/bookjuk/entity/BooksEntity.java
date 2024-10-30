package com.itwillbs.bookjuk.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.itwillbs.bookjuk.domain.books.BookStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class BooksEntity {
	
	// 책번호
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookNum;
	
	// 입고일
	@CreationTimestamp
	private Timestamp bookDate;
	
	// 지점명
	@Column(nullable = false)
	private String storeName; 
	
	// 대여금액
	@Column(nullable = false)
	private Long rentMoney;

	// 도서상태
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BookStatus bookStatus;
	
	// ISBN번호
	@Column(nullable = false)
	private Long ISBN;
	
	// 대여현황
	@Column(nullable = false)
	private Boolean rentStatus;
	
	
}
