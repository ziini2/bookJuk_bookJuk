package com.itwillbs.bookjuk.entity.pay;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.itwillbs.bookjuk.entity.rent.Overdue;
import com.itwillbs.bookjuk.domain.pay.PointPayStatus;
import com.itwillbs.bookjuk.entity.UserContentEntity;
import com.itwillbs.bookjuk.entity.event.CouponEntity;

import com.itwillbs.bookjuk.entity.rent.RentEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "point_deal")
public class PointDealEntity {
	 
	//포인트거래ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "point_pay_id")
	private Long pointPayID;
	
	//포인트거래금액
	@Column(nullable = false)
	private int pointPrice;
	
	//포인트거래상태
	@Column(nullable = false, name = "point_pay_status")
	@Enumerated(EnumType.STRING)
	private PointPayStatus pointPayStatus;
	 
	//요청일시
	@CreationTimestamp
	private LocalDateTime reqDate;
		
	//포인트거래품목
	@Column(nullable = false)
	private String pointPayName;
	
	//유저번호(user_content 테이블 참조)
	@ManyToOne
	@JoinColumn(name = "member_num")
	private UserContentEntity userContentEntity;  // UserContent 테이블 참조
	
	//쿠폰 아이디
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private CouponEntity couponId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "over_num")
	private Overdue overdue;  // 단방향 참조

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rent_num")
	private RentEntity rent;  // 단방향 참조
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id")
	private PaymentEntity paymentEntity;  // 단방향 참조
  

}
