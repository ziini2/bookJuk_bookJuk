package com.itwillbs.bookjuk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor  
@AllArgsConstructor 
@Table(name = "user_content")
public class UserContentEntity {
	
	//유저번호
	@Id
    private Long userNum;  // UserEntity의 기본 키와 공유하는 기본 키

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId  // UserEntity의 ID를 그대로 사용
    @JoinColumn(name = "userNum")
    private UserEntity userEntity;
	
	//관심 설정
	@Column(nullable = true)
	private boolean likeBook; 
		
	//대여도서 수
	@Column(nullable = false, columnDefinition = "int default 0")
	private int bringBook;

//	//회원 등급 테이블의 회원등급아이디 참조
//	@OneToOne
//	@JoinColumn(name = "userGradeID", nullable = false)  // Member 테이블의 PK를 외래 키로 사용
//	private User_grade user_grade;  //users 테이블 참조

	//포인트
	@Column(nullable = false, columnDefinition = "int default 0")
	private int userPoint;

    //포인트값
    public int getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(int userPoint) {
        this.userPoint = userPoint;
    }
}
		