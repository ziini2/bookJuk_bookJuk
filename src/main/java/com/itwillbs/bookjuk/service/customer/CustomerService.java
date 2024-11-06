package com.itwillbs.bookjuk.service.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.repository.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@Log
@RequiredArgsConstructor
public class CustomerService {
	
	private final StoreRepository storeRepository;

	public Page<StoreEntity> getStoreList(Pageable pageable) {
		return storeRepository.findAll(pageable);
	}

	public void addStore(StoreEntity storeEntity) {
		storeRepository.save(storeEntity);
	}

	public Page<StoreEntity> findByStoreNameContaining(Pageable pageable, String search) {
		return storeRepository.findByStoreNameContaining(pageable, search);
	}
	
	
}
