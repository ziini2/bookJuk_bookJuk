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
	private final PasswordEncoder passwordEncoder;

	public Optional<UserEntity> findByuserId(String userId) {
		log.info("MemberpageService findByuserId()");
		
		return memberpageRepository.findByuserId(userId);
	}
	
	public UserEntity findByIdAndPass(UserEntity userEntity) throws Exception{
		log.info("MemberpageService findByIdAndPass()");
		
		UserEntity userEntity2 = memberpageRepository.findById(userEntity.getUserNum())
				.orElseThrow(()-> new Exception("회원 없음"));
		log.info(userEntity2.toString());
		
		boolean match = passwordEncoder.matches(userEntity.getUserPassword(), userEntity2.getUserPassword());
		System.out.println("passwordEncoder.matches : " + match);
		
		if(match == true) {
			//비밀번호 일치
			return userEntity2;
		}else {
			//비밀번호 틀림
			return null;
		}
		
	}

	public void userctupdateMember(UserEntity userEntity) {
		log.info("MemberpageService userctupdateMember()");
		
		memberpageRepository.save(userEntity);
	}

}
