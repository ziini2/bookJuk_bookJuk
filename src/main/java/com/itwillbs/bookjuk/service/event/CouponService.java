package com.itwillbs.bookjuk.service.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.bookjuk.dto.CouponDTO;
import com.itwillbs.bookjuk.entity.UserContentEntity;
import com.itwillbs.bookjuk.entity.event.CouponEntity;
import com.itwillbs.bookjuk.repository.UserContentRepository;
import com.itwillbs.bookjuk.repository.event.CouponRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {
	
	private final CouponRepository couponRepository;
	private final UserContentRepository userContentRepository;
	
	public Page<CouponDTO> getAllCoupon(Long userNum, Pageable pageable) {
		try {
			// 쿠폰 관리 페이지
			if(userNum == 0L) {
				return couponRepository.findAll(pageable).map(this::convertToDto);
			}else {
				// 유저 쿠폰 페이지
				return couponRepository.findByUserNum_UserNum(userNum, pageable).map(this::convertToDto);
			}	        
	    } catch (Exception e) {
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
				.couponNum(couponEntity.getCouponNum())
				.couponPeriod(couponEntity.getCouponPeriod())
				.couponStatus(couponEntity.getCouponStatus())
				.couponType(couponEntity.getCouponType())
				.eventTitle(couponEntity.getEventId().getEventTitle())
				.userId(couponEntity.getUserNum().getUserId())
				.build();
	}

	public Page<CouponDTO> getFilteredCoupon(Long userNum, String searchCriteria, String searchKeyword,
			List<Map<String, String>> filter, Pageable pageable) {
		try {
	        return couponRepository.couponTable(userNum, searchCriteria, searchKeyword, 
	        		filter, pageable).map(this::convertToDto);
	    } catch (Exception e) {
	        log.error("Error fetching filtered events: ", e);
	        return Page.empty(pageable); // 예외 발생 시 빈 페이지 반환
	    }
	}

	// 쿠폰 상세 모달창에 표시할 정보
	public CouponDTO getCouponDetail(Long couponId) {
		CouponEntity couponEntity = couponRepository.findById(couponId).orElse(null);
		return CouponDTO.builder()
				.couponId(couponEntity.getCouponId())
				.eventId(couponEntity.getEventId().getEventId())
//				.eventConditionId(couponEntity.getEventConditionId().getEventConditionId())
//				.notiId(couponEntity.getNotiId().getNotiId())
//				.userNum(couponEntity.getUserNum().getUserNum())
				.couponNum(couponEntity.getCouponNum())
				.couponPeriod(couponEntity.getCouponPeriod())
				.couponStatus(couponEntity.getCouponStatus())
				.couponType(couponEntity.getCouponType())
				.eventTitle(couponEntity.getEventId().getEventTitle())
				.userId(couponEntity.getUserNum().getUserId())
				.build();
	}
	
	// 매일 자정 쿠폰 유효기간 체크
	@Scheduled(cron = "10 0 0 * * ?") // 00:00:10 에 동작
	@Transactional
	public void checkCouponPeriod() {
		LocalDateTime now = LocalDateTime.now();

	    // 현재 시간 이전에 만료된, 상태가 '유효'인 쿠폰을 조회
	    List<CouponEntity> expiredCoupons = couponRepository.findByCouponPeriodBeforeAndCouponStatus(now, "유효");
	    
	    // 쿠폰 상태를 '만료'로 설정
	    expiredCoupons.forEach(coupon -> {
	        coupon.setCouponStatus("만료");
	    });

	    couponRepository.saveAll(expiredCoupons);
	}

	public boolean getCouponByIdAndUserNum(Long couponId, Long userNum) {
		return couponRepository.existsByCouponIdAndUserNum_UserNum(couponId, userNum);
	}

	
	// 유저 pk, 쿠폰 번호, 쿠폰 상태가 유효 인지 조회해서 데이터가 있으면 쿠폰 상태 완료로 변경
	@Transactional
	public boolean useCoupon(Long userNum, String couponNum) {
		Optional<CouponEntity> couponOpt = couponRepository.findByUserNum_UserNumAndCouponNumAndCouponStatus(userNum, couponNum, "유효");

        if (couponOpt.isPresent()) {
            CouponEntity coupon = couponOpt.get();
            coupon.setCouponStatus("완료");
            couponRepository.save(coupon);
            UserContentEntity userContentEntity = userContentRepository.findByUserEntity_UserNum(userNum);
            // 숫자만 추출
            String result = coupon.getEventConditionId().getEventClearReward();
            int couponReward = Integer.parseInt(result.replaceAll("[^\\d]", ""));
            int reward = userContentEntity.getUserPoint() + couponReward;
            userContentEntity.setUserPoint(reward);
            userContentRepository.save(userContentEntity);
            return true;
        }
        return false;
	}
	
	

}
