package com.itwillbs.bookjuk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.books.BooksEntity;


public interface BooksRepository extends JpaRepository<BooksEntity, Long> {
	
	//대여등록 회원검색
	 List<BooksEntity> findByBookInfoEntity_BookNum(Long bookNum);
//    List<BooksEntity> findByStoreNameContainingIgnoreCase(String storeName);
	List<BooksEntity> findByStoreEntity_StoreCode(Long storeCode);
	
}
