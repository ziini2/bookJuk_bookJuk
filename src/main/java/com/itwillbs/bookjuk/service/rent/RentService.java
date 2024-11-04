package com.itwillbs.bookjuk.service.rent;

import java.util.Optional;

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
	
	public void updateReturnInfo(Integer rentNum, String returnInfo) {
		log.info("RentService updateReturnInfo() - rentNum: " + rentNum + ", returnInfo: " + returnInfo);
		
		Optional<RentEntity> rentOpt = rentRepository.findById(rentNum);
		if (rentOpt.isPresent()) {
			RentEntity rent = rentOpt.get();
			rent.setReturnInfo(returnInfo);  // RentEntity에 returnInfo 필드가 존재해야 합니다.
			rentRepository.save(rent);
		} else {
			throw new IllegalArgumentException("Invalid rent number: " + rentNum);
		}
	}
	
}
