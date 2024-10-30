package com.itwillbs.bookjuk.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
public class StoreEntity {

	// 지점번호
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long storeCode;

	// 지점이름
	private String storeName;

	// 지점 전체주소
	private String storeLocation;

	// 지점 동주소
	@Column(nullable = true)
	private String storeLocation2;

	// 지점 전화번호
	private String storeTel;

	// 지점 대표자명
	private String storeRepresent;

	// 지점 이메일
	private String storeEmail;

	// 지점 사업자등록번호
	private String storeRegiNum;

	// 지점 등록일
	@CreationTimestamp
	private String storeRegiDate;

	// 지점 정보 수정일
	@UpdateTimestamp
	private String storeUpdateDate;
}
