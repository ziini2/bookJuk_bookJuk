package com.itwillbs.bookjuk.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;


public interface BooksRepository extends JpaRepository<BooksEntity, Long> {
	
	 //대여등록 회원검색
	 List<BooksEntity> findByBookInfoEntity_BookNum(Long bookNum);

	 // 지점코드
	 List<BooksEntity> findByStoreEntity_StoreCode(Long storeCode);
	
	 // 장바구니
	 List<BooksEntity> findByBookInfoEntity(BookInfoEntity bookInfoEntity);

	 //전체 대여가능 목록 데이터 가져오기
	 Page<BooksEntity> findAllByRentStatusTrue(Pageable pageable);

}
