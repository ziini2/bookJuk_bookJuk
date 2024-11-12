package com.itwillbs.bookjuk.entity.books;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.itwillbs.bookjuk.domain.books.BookStatus;
import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "books")
public class BooksEntity {
	
	// Books 엔티티의 고유 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long booksId;  // 고유한 식별자 (예: 도서 관리 레코드의 PK)
	
	// 도서상태
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BookStatus bookStatus;

	// 대여현황
	@Column(nullable = false)
	private Boolean rentStatus;
	
	@ManyToOne
    @JoinColumn(name = "bookNum", referencedColumnName = "bookNum", nullable = false) // 외래 키 설정
    private BookInfoEntity bookInfoEntity; 
	
	 // 지점 ID (StoreEntity와 연결)
    @ManyToOne
    @JoinColumn(name = "storeCode", referencedColumnName = "storeCode", nullable = false) // 외래 키 설정
    private StoreEntity storeEntity; 
	
	
	// 입고일
	@CreationTimestamp
	private Timestamp bookDate;
	
	// 수정일
	@UpdateTimestamp
	private Timestamp bookUpdate;
	
	// 재고
	@Column(nullable = false)
	private Long inventory;
		

 // 빌더 패턴을 사용할 때 외래 키 관계를 설정
    public static BooksEntity createBooksEntity(BookInfoEntity bookInfoEntity, BookStatus bookStatus, 
                                                 Boolean rentStatus, Long inventory, 
                                                 Long storeCode, StoreEntity store) {
        return BooksEntity.builder()
                .bookStatus(bookStatus)
                .rentStatus(rentStatus)
                //.bookInfo(bookInfoEntity)  // 외래 키 관계 설정
                .storeEntity(store)  // 지점 설정
                .build();
    }

	


public Long getBooksId() {
	return booksId;
}


public void setBooksId(Long booksId) {
	this.booksId = booksId;
}




public StoreEntity getStoreEntity() {
	return storeEntity;
}




public void setStoreEntity(StoreEntity storeEntity) {
	this.storeEntity = storeEntity;
}
}
