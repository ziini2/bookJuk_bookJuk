package com.itwillbs.bookjuk.entity.pay;

import java.time.LocalDateTime;

import com.itwillbs.bookjuk.domain.pay.PointPayStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
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
	private Long pointPrice;
	
	//포인트거래상태
	@Column(nullable = false, name = "point_pay_status")
	@Enumerated(EnumType.STRING)
	private PointPayStatus pointPayStatus;
	 
	//요청일시
	@Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime reqDate;
}
