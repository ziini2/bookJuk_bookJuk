package com.itwillbs.bookjuk.repository.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.event.CouponEntity;

public interface UserCouponRepository extends JpaRepository<CouponEntity, Long> {

	Page<CouponEntity> findByUser_UserNum(Long userNum, Pageable pageable);

}
