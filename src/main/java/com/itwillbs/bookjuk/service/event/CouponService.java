package com.itwillbs.bookjuk.service.event;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.dto.CouponDTO;
import com.itwillbs.bookjuk.entity.event.CouponEntity;
import com.itwillbs.bookjuk.repository.event.CouponRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {
	
	private final CouponRepository couponRepository;
	
	public Page<CouponDTO> getAllEvent(Pageable pageable) {
		try {
	        return couponRepository.findAll(pageable).map(this::convertToDto);
	    } catch (Exception e) {
	        // 로그 출력 및 예외 처리
	        log.error("Error fetching all notis: ", e);
	        return Page.empty(pageable);
	    }
	}
	
	private CouponDTO convertToDto(CouponEntity couponEntity) {
		return CouponDTO.builder()
				.couponId(couponEntity.getCouponId())
//				.eventId(couponEntity.getEventId().getEventId())
//				.eventConditionId(couponEntity.getEventConditionId().getEventConditionId())
//				.notiId(couponEntity.getNotiId().getNotiId())
//				.userNum(couponEntity.getUserNum().getUserNum())
//				.couponNum(couponEntity.getCouponNum())
				.couponPeriod(couponEntity.getCouponPeriod())
				.couponStatus(couponEntity.getCouponStatus())
				.couponType(couponEntity.getCouponType())
				.eventTitle(couponEntity.getEventId().getEventTitle())
				.userId(couponEntity.getUserNum().getUserId())
				.build();
	}

	public Page<CouponDTO> getFilteredEvent(String searchCriteria, String searchKeyword,
			List<Map<String, String>> filter, Pageable pageable) {
		try {
	        return couponRepository.couponTable(searchCriteria, searchKeyword, 
	        		filter, pageable).map(this::convertToDto);
	    } catch (Exception e) {
	        // 로그 출력 및 예외 처리
	        log.error("Error fetching filtered events: ", e);
	        return Page.empty(pageable); // 예외 발생 시 빈 페이지 반환
	    }
	}

	public CouponDTO getCouponDetail(Long couponId) {
		CouponEntity couponEntity = couponRepository.findById(couponId).orElse(null);
		return CouponDTO.builder()
				.couponId(couponEntity.getCouponId())
				.eventId(couponEntity.getEventId().getEventId())
				.eventConditionId(couponEntity.getEventConditionId().getEventConditionId())
				.notiId(couponEntity.getNotiId().getNotiId())
				.userNum(couponEntity.getUserNum().getUserNum())
				.couponNum(couponEntity.getCouponNum())
				.couponPeriod(couponEntity.getCouponPeriod())
				.couponStatus(couponEntity.getCouponStatus())
				.couponType(couponEntity.getCouponType())
				.build();
	}
	
	

}
