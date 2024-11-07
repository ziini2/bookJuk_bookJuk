package com.itwillbs.bookjuk.entity.pay;

import java.time.LocalDateTime;

import com.itwillbs.bookjuk.domain.pay.PaymentStatus;
import com.itwillbs.bookjuk.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment")
public class PaymentEntity {
 
	//결제ID
	@Id
	private String paymentId;
	
	//유저번호(users 테이블 참조)
	@ManyToOne
	@JoinColumn(name = "userNum", nullable = false)
	private UserEntity userEntity;  // Users 테이블 참조
	
	//결제상태
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;
	
	//결제금액
	@Column(nullable = false)
	private Long paymentPrice;
	
	//요청일시
	@Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime reqDate;
	
	//결제수단
	@Column(nullable = false)
	private String paymentMethod;
	
	//주문번호
	@Column(nullable = false)
	private String merchantUid;
	
	//결제품목
	@Column(nullable = false)
	private String priceName;
}


