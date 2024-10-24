package com.itwillbs.bookjuk.entity.event;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "coupon")
@Getter
@Setter
@ToString
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "couponId", nullable = false)
	private Long couponId;
	
	@Column(name = "couponNum", length = 30, unique = true, nullable = false)
	private String couponNum;
	
	@Column(name = "couponPeriod", nullable = false)
	private Timestamp couponPeriod;
	
	@Column(name = "couponStatus", length = 5, nullable = false)
	private String couponStatus;
	
	
	
	
	
	
	
	
	
}
