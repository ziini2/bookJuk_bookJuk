package com.itwillbs.bookjuk.repository.event;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itwillbs.bookjuk.entity.event.CouponEntity;

public interface CouponRepositoryCustom {

	// 쿠폰 페이지 리스트 출력
	Page<CouponEntity> couponTable(Long userNum, 
								   String searchCriteria, 
								   String searchKeyword, 
								   List<Map<String, String>> filter, 
								   Pageable pageable);
	
}
