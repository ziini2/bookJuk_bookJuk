package com.itwillbs.bookjuk.service.rent;

import java.sql.Timestamp;
import java.time.Instant;
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
	
	public void updateReturnInfo(Long rentNum, String returnInfo) {
        log.info("RentService updateReturnInfo() - rentNum: " + rentNum + ", returnInfo: " + returnInfo);
        
        Optional<RentEntity> rentOpt = rentRepository.findById(rentNum);
        if (rentOpt.isPresent()) {
            RentEntity rent = rentOpt.get();
            rent.setReturnInfo(returnInfo);

            // returnInfo가 "반납완료"인 경우에만 returnDate를 현재 시간으로 설정
            if ("반납완료".equals(returnInfo)) {
                rent.setReturnDate(Timestamp.from(Instant.now()));
            } else {
                // 반납 상태가 "반납완료"가 아닌 경우 returnDate를 null로 설정할 수 있음
                rent.setReturnDate(null);
            }
            
            rentRepository.save(rent);
        } else {
            throw new IllegalArgumentException("잘못된 대여 번호입니다: " + rentNum);
        }
    }
	
}
