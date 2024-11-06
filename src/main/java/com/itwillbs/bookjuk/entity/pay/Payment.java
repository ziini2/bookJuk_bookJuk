package com.itwillbs.bookjuk.entity.pay;

import java.time.LocalDateTime;

import com.itwillbs.bookjuk.domain.pay.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table (name = "payment")
public class Payment {
 
	//결제ID
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String payment_id;
	
	//유저 번호
	@Column(nullable = false)
	private Long user_num;
	
	//결제상태
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PaymentStatus payment_status;
	
	//결제금액
	@Column(nullable = false)
	private Long payment_price;
	
	//요청일시
	@Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime req_date;
	
	//결제수단
	@Column(nullable = false)
	private String payment_method;
	
	//주문번호
	@Column(nullable = false)
	private String merchant_uid;
	
	//상품이름
	@Column(nullable = false)
	private String price_name;
}


