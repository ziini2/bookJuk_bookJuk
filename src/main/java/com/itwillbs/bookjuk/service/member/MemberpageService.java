package com.itwillbs.bookjuk.service.member;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
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

	public Optional<UserEntity> findByuserId(String userId) {
		log.info("MemberService findByuserId()");
		
		return memberpageRepository.findByuserId(userId);
	}

}
