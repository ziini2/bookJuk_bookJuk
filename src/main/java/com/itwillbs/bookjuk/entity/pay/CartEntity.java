package com.itwillbs.bookjuk.entity.pay;

import java.time.LocalDateTime;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class CartEntity {
	
	//장바구니ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_item_id")
	private int cartItemID;
	
	//도서번호(books 테이블 참조)
	@ManyToOne
	@JoinColumn(name = "bookNum", nullable = false)
	private BooksEntity booksEntity;  // Users 테이블 참조
	
	//수량
	@Column(nullable = false, columnDefinition = "int default 1")
	private int itemQuantity;

	//장바구니생성일자
	@Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime cartDate;
	
	//포인트거래ID
	@ManyToOne
	@JoinColumn(name = "pointPayId", nullable = false)
	private PaymentEntity paymentEntity;  // Users 테이블 참조
	
	//유저번호(users 테이블 참조)
	@ManyToOne
	@JoinColumn(name = "userNum", nullable = false)
	private UserEntity userEntity;  // Users 테이블 참조
}
