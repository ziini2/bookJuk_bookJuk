package com.itwillbs.bookjuk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.StoreEntity;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

	Page<StoreEntity> findByStoreNameContaining(Pageable pageable, String search);

}
