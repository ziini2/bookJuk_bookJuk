package com.itwillbs.bookjuk.service.member;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.repository.MemberpageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class MemberpageService {
	
	private final MemberpageRepository memberpageRepository;
	
//	public Optional<UserEntity> findById(Long userNum) {
//		log.info("MemberpageService findByIdAfindByIdndPass()");
//		
//		return memberpageRepository.findById(userNum);
//	}

	public UserEntity findByUserId(String userId) {
		log.info("MemberpageService findByUserId()");
		return memberpageRepository.findByUserId(userId);
	}

}
