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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "rent")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rentNum;
	
	@Column(nullable = false)
	private Long userNum;
	
	@Column(nullable = false)
	private Long bookNum;
	
	@Column(nullable = false)
	private String userId;

	@Column(nullable = false)
	private String userName;
	
	@Column(nullable = false)
	private String userPhone;
	
	@Column(nullable = false)
	private String bookName;
	
	//대여일 2024-10-10
	@CreationTimestamp
	private Timestamp rentDate;
	
	//반납일 2024-10-17
//	@UpdateTimestamp
	private Timestamp returnDate;
	
	@Column(nullable = false)
	private String returnInfo;
	
}
