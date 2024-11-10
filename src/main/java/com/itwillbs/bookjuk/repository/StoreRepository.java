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

	Page<StoreEntity> findByStoreNameContaining(Pageable pageable, String search);

	@Transactional
	@Modifying
	@Query("UPDATE StoreEntity s SET s.storeStatus = 'close' WHERE s.storeCode = :storeCode")
	void deleteStore(@Param("storeCode") Long storeCode);
}
