package com.itwillbs.bookjuk.service.rent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class RentService {
	
	public Page<Rent> getRentList(Pageable pageable) {
		log.info("RentService getRentList()");
		
		return rentRepository.findAll(pageable);
	}
	
	
}
