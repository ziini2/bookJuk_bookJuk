package com.itwillbs.bookjuk.service.customer;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.repository.StoreRepository;
import com.itwillbs.bookjuk.repository.UserContentRepository;
import com.itwillbs.bookjuk.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {
	
	private final StoreRepository storeRepository;
	
	private final UserRepository userRepository;
	
	private final UserContentRepository userContentRepository;
	
	public Page<StoreEntity> getStoreList(Pageable pageable) {
		return storeRepository.findAll(pageable);
	}

	// 지점 등록 함수
	public void addStore(StoreEntity storeEntity) {
		storeRepository.save(storeEntity);
	}
	
	// 검색어포함 페이지네이션 함수
	public Page<StoreEntity> findByStoreNameContaining(Pageable pageable, String search) {
		return storeRepository.findByStoreNameContaining(pageable, search);
	}
	
	// 한 지점정보를 가져오는 함수
	public Optional<StoreEntity> findById(Long storeCode) {
		return storeRepository.findById(storeCode);
	}

	public void storeUpdate(StoreEntity storeEntity) {
		storeEntity.setStoreStatus("open");
		storeRepository.save(storeEntity);
	}
	
	public void deleteStore(Long storeCode, String status) {
		storeRepository.deleteStore(storeCode, status);
	}

	public List<UserEntity> getMemberList() {
		return userRepository.findAll();
	}

	public UserEntity getUserInfo(Long userNum) {
		return userRepository.getReferenceById(userNum);
	}

	public Page<UserEntity> findByUserContaining(Pageable pageable, String search) {
		return storeRepository.findByUserContaining(pageable, search);
	}

//	public UserContentEntity getGrade(Long userNum) {
//		return userContentRepository.findByUserNum(userNum);
//	}

	public void deleteUser(Long string, Boolean status) {
		storeRepository.deleteUser(string, status);
	}
	
	
}
