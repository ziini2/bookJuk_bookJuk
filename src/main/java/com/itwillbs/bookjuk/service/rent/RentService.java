package com.itwillbs.bookjuk.service.rent;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.entity.RentEntity;
import com.itwillbs.bookjuk.repository.RentRepository;

import jakarta.annotation.PostConstruct;
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
		
	// 서버 시작 시 실행되는 초기화 메서드
	@PostConstruct
	public void updateOverdueRentalsOnStartup() {
		log.info("서버 시작 시 연체 상태 업데이트 시작");
		updateOverdueRentals();
		log.info("서버 시작 시 연체 상태 업데이트 완료");
	}
	
	@Scheduled(cron = "0 0 0 * * ?")
	public void updateOverdueRentals() {
	    log.info("연체 상태 업데이트 시작");

	    LocalDate oneWeekAgoDate = LocalDate.now().minusDays(7);
	    Timestamp oneWeekAgoTimestamp = Timestamp.valueOf(oneWeekAgoDate.atStartOfDay());

	    List<RentEntity> overdueRentals = rentRepository.findByReturnDateIsNullAndRentDateBefore(oneWeekAgoTimestamp);
	    for (RentEntity rent : overdueRentals) {
	        rent.setReturnInfo("연체중");
	        rentRepository.save(rent);
	    }

	    log.info("연체 상태 업데이트 완료");
	}
	
//	public List<RentEntity> searchByCriteria(String criteria, String keyword) {
//		log.info("searchByCriteria called with criteria: " + criteria + ", keyword: " + keyword);
//        switch (criteria) {
//            case "userName":
//                return rentRepository.findByUserNameContainingOrderByRentNumDesc(keyword);
//            case "userId":
//                return rentRepository.findByUserIdContainingOrderByRentNumDesc(keyword);
//            case "bookName":
//                return rentRepository.findByBookNameContainingOrderByRentNumDesc(keyword);
//            default:
//                return new ArrayList<>(); // 조건에 맞지 않으면 빈 리스트 반환
//        }
//    }
	
	public Page<RentEntity> searchByCriteria(String criteria, String keyword, Pageable pageable) {
	    log.info("searchByCriteria 호출됨 - 기준: " + criteria + ", 키워드: " + keyword);
	    switch (criteria) {
	        case "userName":
	            return rentRepository.findByUserNameContaining(keyword, pageable);
	        case "userId":
	            return rentRepository.findByUserIdContaining(keyword, pageable);
	        case "bookName":
	            return rentRepository.findByBookNameContaining(keyword, pageable);
	        default:
	            return Page.empty(pageable); // 조건이 맞지 않으면 빈 페이지 반환
	    }
	}

    
   
	
}
