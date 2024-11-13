package com.itwillbs.bookjuk.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    
	//회원번호 pk
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNum;

	//유저번호(user 테이블 참조)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNum")
    private UserEntity userEntity;
   
   //관심 설정
   @Column(nullable = true)
   private boolean likeBook; 
      
   //대여도서 수
   @Column(nullable = false, columnDefinition = "int default 0")
   private int bringBook;

//   //회원 등급 테이블의 회원등급아이디 참조
//   @OneToOne
//   @JoinColumn(name = "userGradeID", nullable = false)  
//   private UserGradeEntity userGradeEntity;  //users 테이블 참조

   //포인트
   @Column(nullable = false)
   private int userPoint = 0;  // 기본값을 0으로 설정

    //포인트값
    public int getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(int userPoint) {
        this.userPoint = userPoint;
    }
}
      