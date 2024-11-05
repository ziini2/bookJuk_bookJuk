package com.itwillbs.bookjuk.entity;

import com.itwillbs.bookjuk.domain.login.LoginType;
import com.itwillbs.bookjuk.domain.login.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //유저테이블 PK
    private Long userNum;

    //유저 ID
    @Column(unique = true, nullable = false)
    private String userId;

    //유저 password
    private String userPassword;

    //유저 이름
    private String userName;

    //유저 생년월일 1994-10-00(패턴)
    private String userBirthday;

    //유저 email
    @Column(unique = true, nullable = false)
    private String userEmail;

    //유저 휴대폰번호 010-0000-0000(패턴)
    @Column(unique = true, nullable = false)
    private String userPhone;

    //유저 Role 값 (enum 클래스의 정의된 것만 사용)
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(255)")
    private UserRole userRole;

    //생성일
    @CreationTimestamp //최초 영속화 (INSERT) 시점에 현재 시간을 저장
    private Timestamp createDate;

    //수정일
    @UpdateTimestamp //엔터티가 수정(UPDATE) 될 때마다 현재 시간으로 갱신
    private Timestamp updateDate;

    //유저 LoginType (enum 클래스의 정의된 것만 사용)
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(255)")
    private LoginType loginType;

    //약관 동의 여부
    private boolean accepted;

    //유저 활성상태(탈퇴회원 여부 판단)
    private boolean activate;
}
