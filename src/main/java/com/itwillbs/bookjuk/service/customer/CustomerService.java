package com.itwillbs.bookjuk.service.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.repository.StoreRepository;
import com.itwillbs.bookjuk.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@Log
@RequiredArgsConstructor
public class CustomerService {
	
	private final StoreRepository storeRepository;
	
	private final UserRepository userRepository;
	
	public Page<StoreEntity> getStoreList(Pageable pageable) {
		return storeRepository.findAll(pageable);
	}

	public void addStore(StoreEntity storeEntity) {
		storeRepository.save(storeEntity);
	}

	public Page<StoreEntity> findByStoreNameContaining(Pageable pageable, String search) {
		return storeRepository.findByStoreNameContaining(pageable, search);
	}

	public Optional<StoreEntity> findById(Long storeCode) {
		return storeRepository.findById(storeCode);
	}

	public void storeUpdate(StoreEntity storeEntity) {
		storeRepository.save(storeEntity);
	}
	
	public void deleteStore(Long storeCode) {
		storeRepository.deleteStore(storeCode);
	}

	public List<UserEntity> getMemberList() {
		return userRepository.findAll();
	}

	public UserEntity getUserInfo(Long userNum) {
		return userRepository.getById(userNum);
	}
	
	
}
