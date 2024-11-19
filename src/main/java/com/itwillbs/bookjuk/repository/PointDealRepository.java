package com.itwillbs.bookjuk.repository;

import com.itwillbs.bookjuk.entity.pay.PointDealEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PointDealRepository extends JpaRepository<PointDealEntity, Long> {

    Page<PointDealEntity> findByUserContentEntity_UserEntity_UserNum(Long userNum, Pageable pageable);

    // dashboard에서 사용
    @Query("SELECT SUM(p.pointPrice) FROM PointDealEntity p WHERE p.reqDate BETWEEN :startOfDay AND :endOfDay AND p.pointPayName IN ('대여료', '연체료')")
    Optional<Long> sumAmountByReqDateBetween(@Param("startOfDay") LocalDateTime startOfDay,
                                             @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT p FROM PointDealEntity p WHERE p.reqDate BETWEEN :startDate AND :endDate AND p.pointPayName IN :pointOptions ORDER BY p.reqDate DESC")
Optional<List<PointDealEntity>> findAllByReqDateBetweenAndPointPayNameInOrderByReqDateDesc(@Param("startDate") LocalDateTime startDate,
                                                                                          @Param("endDate") LocalDateTime endDate,
                                                                                          @Param("pointOptions") List<String> pointOptions);

    @Query("SELECT p FROM PointDealEntity p WHERE p.reqDate BETWEEN :startDate AND :endDate AND p.pointPayName IN :pointOptions ORDER BY p.reqDate DESC")
    Page<PointDealEntity> findAllByReqDateBetweenAndPointPayNameInOrderByReqDateDescPage(@Param("startDate") LocalDateTime startDate,
                                                                                               @Param("endDate") LocalDateTime endDate,
                                                                                               @Param("pointOptions") List<String> pointOptions, Pageable pageable);

    Page<PointDealEntity> findAllFirstByReqDateBetweenOrderByReqDateDesc(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<PointDealEntity> findAllFirstByReqDateBetweenAndPointPayNameOrderByReqDateDesc(LocalDateTime startDate, LocalDateTime endDate, String pointOption, Pageable pageable);

    Optional<List<PointDealEntity>> findAllFirstByReqDateBetweenAndPointPayNameOrderByReqDateDesc(LocalDateTime startDate, LocalDateTime endDate, String pointOption);
}
