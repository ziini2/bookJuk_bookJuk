package com.itwillbs.bookjuk.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "store")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StoreEntity {

	// 지점번호
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "storeCode", nullable = false)
	private Long storeCode;

	// 지점이름
	@Column(name = "storeName", nullable = false)
	private String storeName;

	// 지점 전체주소
	@Column(name = "storeLocation", nullable = false)
	private String storeLocation;

	// 지점 동주소
	@Column(name = "storeLocation2", nullable = true)
	private String storeLocation2;

	// 지점 전화번호
	@Column(name = "storeTel", nullable = false)
	private String storeTel;

	// 지점 대표자명
	@Column(name = "storeRepresent", nullable = false)
	private String storeRepresent;

	// 지점 이메일
	@Column(name = "storeEmail", nullable = false)
	private String storeEmail;

	// 지점 사업자등록번호
	@Column(name = "storeRegiNum", nullable = false)
	private String storeRegiNum;

	// 지점 등록일
	@CreationTimestamp
	@Column(name = "storeRegiDate", nullable = false)
	private Timestamp storeRegiDate;

	// 지점 정보 수정일
	@UpdateTimestamp
	@Column(name = "storeUpdateDate", nullable = false)
	private Timestamp storeUpdateDate;
}
