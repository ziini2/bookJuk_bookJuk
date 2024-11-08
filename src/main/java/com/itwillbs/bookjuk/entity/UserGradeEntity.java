package com.itwillbs.bookjuk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_grade")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserGradeEntity {
	
	// 등급번호 pk
	@Id
	@Column(name = "user_grade_id")
	private Long userGradeID;
	
	// 등급별마일리지
	@Column(nullable = true)
	private Long gradePoint;
	
	// 등급한도
	@Column(nullable = true)
	private Long gradeMax;
		
	// 등급명
	@Column(nullable = false)
	private String gradeName;
}
