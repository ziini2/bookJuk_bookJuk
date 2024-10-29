package com.itwillbs.bookjuk.service.rent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.entity.RentEntity;
import com.itwillbs.bookjuk.repository.RentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class RentService {
	
	private final RentRepository rentRepository;
	
	public Page<RentEntity> getRentList(Pageable pageable) {
		log.info("RentService getRentList()");
		
		return rentRepository.findAll(pageable);
	}
	
	
}
