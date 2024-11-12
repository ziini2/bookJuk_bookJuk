package com.itwillbs.bookjuk.entity.books;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.itwillbs.bookjuk.entity.rent.RentEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.itwillbs.bookjuk.domain.books.BookStatus;
import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "books")
public class BooksEntity {

	// Books 엔티티의 고유 PK
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long booksId; // 고유한 식별자 (예: 도서 관리 레코드의 PK)

	// 도서상태
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BookStatus bookStatus;

	// 대여현황
	@Column(nullable = false)
	private Boolean rentStatus;

	// bookINfoEntity 의 bookNum 을 참조해서 정보를 가지고온다.
	@ManyToOne
	@JoinColumn(name = "bookNum", referencedColumnName = "bookNum", nullable = false) // 외래 키 설정
	private BookInfoEntity bookInfoEntity;
 
	// 지점 ID (StoreEntity와 연결)
	@ManyToOne
	@JoinColumn(name = "storeCode", referencedColumnName = "storeCode", nullable = false) // 외래 키 설정
	private StoreEntity storeEntity;

	// 수정일
	@UpdateTimestamp
	private Timestamp bookUpdate;

	// 재고
	@Column(nullable = false)
	private Long inventory;

	// rent 테이블과 양방향 관계 설정
	@OneToMany(mappedBy = "book")
	private List<RentEntity> books = new ArrayList<>();

}
