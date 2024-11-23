package com.itwillbs.bookjuk.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.pay.PaymentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
	PaymentEntity findByPaymentId(String paymentId);
	List<PaymentEntity> findAllByOrderByReqDateDesc(); //모든 결제 정보 조회 
	List<PaymentEntity> findByUserContentEntity_UserEntity_UserNumOrderByReqDateDesc(Long memberNum); //사용자 본인 결제 정보만 조회

	@Query("SELECT SUM(p.paymentPrice) FROM PaymentEntity p WHERE p.reqDate BETWEEN :startDate AND :endDate")
	Optional<Long> sumAmountByReqDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	Optional<List<PaymentEntity>> findAllByReqDateBetween(LocalDateTime of, LocalDateTime of1);
	
	
	 //@Query("SELECT p FROM PaymentEntity p WHERE p.paymentId LIKE %:search%")
    @Query("SELECT p FROM PaymentEntity p " +
    	       "JOIN p.userContentEntity u " +  // PaymentEntity -> UserContentEntity
    	       "JOIN u.userEntity user " +  // UserContentEntity -> UserEntity
    	       "WHERE (p.paymentId LIKE %:search% OR " +
    	       "p.paymentMethod LIKE %:search% OR " +
    	       "p.priceName LIKE %:search% OR " +
    	       "CAST(user.userNum AS string) LIKE %:search%)")
    	    
    Page<PaymentEntity> findByPaymentIdContaining(Pageable pageable, @Param("search") String search);

}