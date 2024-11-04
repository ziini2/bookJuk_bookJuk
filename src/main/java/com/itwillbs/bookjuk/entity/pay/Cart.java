package com.itwillbs.bookjuk.entity.pay;

import java.time.LocalDateTime;

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
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table (name = "cart")
public class Cart {
	
	//장바구니ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_item_id")
	private int cartItemID;
	
	//수량
	@Column(nullable = false)
	private int itemQuantity;

	
	//장바구니생성일자
	@Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime cartDate;
	
	//포인트거래ID
	@Column(nullable = true, name = "point_pay_id")
	private Long pointPayID;
	
	//유저 번호
	@Column(nullable = false)
	private Long userNum;
}
