package com.itwillbs.bookjuk.repository;

import com.itwillbs.bookjuk.entity.pay.PointDealEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PointDealRepository extends JpaRepository<PointDealEntity, Long> {

    Page<PointDealEntity> findByUserContentEntity_MemberNum(Long userNum, Pageable pageable);

    // dashboard에서 사용
    @Query("SELECT SUM(p.pointPrice) FROM PointDealEntity p WHERE p.reqDate BETWEEN :startOfDay AND :endOfDay")
    Optional<Long> sumAmountByReqDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<List<PointDealEntity>> findAllFirstByReqDateBetweenOrderByReqDateDesc(LocalDateTime of, LocalDateTime of1);


}
