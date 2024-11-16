package com.itwillbs.bookjuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.pay.PointDealEntity;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PointDealRepository extends JpaRepository<PointDealEntity, Long> {


    // dashboard에서 사용
    @Query("SELECT SUM(p.pointPrice) FROM PointDealEntity p WHERE p.reqDate BETWEEN :startOfDay AND :endOfDay")
    Optional<Long> sumAmountByReqDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<List<PointDealEntity>> findAllFirstByReqDateBetweenOrderByReqDateDesc(LocalDateTime of, LocalDateTime of1);
}
