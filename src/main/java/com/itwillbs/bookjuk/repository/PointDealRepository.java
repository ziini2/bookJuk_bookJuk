package com.itwillbs.bookjuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.pay.PointDealEntity;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PointDealRepository extends JpaRepository<PointDealEntity, Long> {


    // dashboard에서 사용
    @Query("SELECT SUM(p.pointPrice) FROM PointDealEntity p WHERE p.reqDate BETWEEN :startOfDay AND :endOfDay")
    Optional<Long> sumAmountByReqDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
