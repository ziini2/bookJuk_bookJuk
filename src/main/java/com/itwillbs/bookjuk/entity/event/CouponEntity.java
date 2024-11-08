package com.itwillbs.bookjuk.entity.event;

import java.sql.Timestamp;

import com.itwillbs.bookjuk.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "coupon")
public class CouponEntity {

	// 쿠폰 아이디
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "couponId", nullable = false)
	private Long couponId;
	
	// 이벤트 아이디
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId")
    private EventEntity eventId;
	
	// 이벤트 조건 아이디
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "eventConditionId")
	private EventConditionEntity eventConditionId;
	
	// 알림 아이디
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notiId")
    private NotificationEntity notiId;
	
	// 유저 PK
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNum")
    private UserEntity userNum;
	
	// 쿠폰 번호
	@Column(name = "couponNum", length = 30, unique = true, nullable = false)
	private String couponNum;
	
	// 쿠폰 유효 기간(1년)
	@Column(name = "couponPeriod", nullable = false)
	private Timestamp couponPeriod;
	
	// 쿠폰 상태(유효, 만료, 완료)
	@Column(name = "couponStatus", length = 5, nullable = false)
	private String couponStatus;
	
	// 쿠폰 종류(1000p, 2000p 등등)
	@Column(name = "couponType", nullable = false)
	private String couponType;
	
	
	
	
	
	
	
	
	
}
