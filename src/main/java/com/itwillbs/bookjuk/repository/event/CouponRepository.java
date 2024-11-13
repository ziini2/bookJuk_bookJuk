package com.itwillbs.bookjuk.repository.event;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.event.CouponEntity;

public interface CouponRepository extends JpaRepository<CouponEntity, Long> {

}
