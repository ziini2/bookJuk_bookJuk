package com.itwillbs.bookjuk.repository.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.bookjuk.entity.event.CouponEntity;
import com.itwillbs.bookjuk.entity.event.EventConditionEntity;

public interface CouponRepository extends JpaRepository<CouponEntity, Long>, CouponRepositoryCustom {

	// 쿠폰 생성시 동일한 쿠폰 번호 있는지
	boolean existsByCouponNum(String coupon);

	// 유저 아이디로 쿠폰 조회
	Page<CouponEntity> findByUserNum_UserNum(Long userNum, Pageable pageable);
	
	// 쿠폰 유효기간이 지나고 상태가 유효인 쿠폰들 만료로 변경
	List<CouponEntity> findByCouponPeriodBeforeAndCouponStatus(LocalDateTime couponPeriod, String couponStatus);

	// 쿠폰 상세 모달창을 url로 접근시 유효성 확인
	boolean existsByCouponIdAndUserNum_UserNum(Long couponId, Long userNum);

	// 쿠폰 사용시 유효성 검사
	Optional<CouponEntity> findByUserNum_UserNumAndCouponNumAndCouponStatus(Long userNum, String couponNum, String couponStatus);
	
	@Query("SELECT DISTINCT c.eventConditionId FROM CouponEntity c WHERE c.eventConditionId.eventConditionId IN :eventConditionIds")
	List<EventConditionEntity> findEventConditionsWithCoupons(@Param("eventConditionIds") List<Integer> eventConditionIds);
	
	
	
//	@Query("SELECT DISTINCT c.eventCondition FROM CouponEntity c WHERE c.eventCondition IN :eventConditions")
//    List<EventConditionEntity> findEventConditionsWithCoupons(@Param("eventConditions") List<EventConditionEntity> eventConditions);


}
