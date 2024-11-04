package com.itwillbs.bookjuk.entity.pay;

import java.time.LocalDateTime;

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
@Table (name = "point_deal")
public class PointDeal {
	
	//포인트거래ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pointPayID;
	
	//포인트거래금액
	@Column(nullable = false)
	private Long pointPrice;
	
	//포인트거래상태
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private String pointPayStatus;
	
	//요청일시
	@Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime reqDate;
}
