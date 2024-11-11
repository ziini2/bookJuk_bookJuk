package com.itwillbs.bookjuk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.bookjuk.entity.StoreEntity;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

//	Page<StoreEntity> findByStoreNameContaining(Pageable pageable, String search);

	// 대여등록 회원검색
	StoreEntity findByStoreName(String storeName);

	// JPQL 쿼리사용, 지점상태 업데이트 쿼리
	// 테이블이름과 컬럼이름은 엔티티클래스를 따라야함
	@Transactional
	@Modifying
	@Query("UPDATE StoreEntity s SET s.storeStatus = 'close' WHERE s.storeCode = :storeCode")
	void deleteStore(@Param("storeCode") Long storeCode);
	
	// 지점 컬럼검색 쿼리
	// 테이블이름과 컬럼이름은 엔티티클래스를 따라야함
	@Query("SELECT s FROM StoreEntity s WHERE " +
		       "s.storeName LIKE %:search% OR " +
		       "s.storeTel LIKE %:search% OR " +
		       "s.storeLocation LIKE %:search% OR " +
		       "s.storeLocation2 LIKE %:search% OR " +
		       "s.storeRegiNum LIKE %:search% OR " +
		       "CAST(s.storeRegiDate AS string) LIKE %:search%") // 타임스탬프는 문자로 변환해서 검색
	Page<StoreEntity> findByStoreNameContaining(Pageable pageable, @Param("search") String search);
}
