package com.itwillbs.bookjuk.service.rent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.repository.SearchRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class SearchService {
	
	@Autowired
    private SearchRepository searchRepository;

    public List<UserEntity> searchUsers(String criteria, String keyword) {
        if ("userName".equals(criteria)) {
            return searchRepository.findByUserNameContainingIgnoreCase(keyword);
        } else if ("userId".equals(criteria)) {
            return searchRepository.findByUserIdContainingIgnoreCase(keyword);
        } else {
            return List.of(); // 조건이 맞지 않으면 빈 리스트 반환
        }
    }
	
	
}
