package com.itwillbs.bookjuk.repository.event;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.event.CouponEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CouponRepositoryImpl implements CouponRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Page<CouponEntity> couponTable(String searchCriteria, 
												 String searchKeyword, 
												 List<Map<String, String>> filter, 
												 Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CouponEntity> query = cb.createQuery(CouponEntity.class);
        Root<CouponEntity> coupon = query.from(CouponEntity.class);
        Join<CouponEntity, UserEntity> userJoin = coupon.join("userNum", JoinType.LEFT);
        List<Predicate> predicates = new ArrayList<>();
        // 검색 조건 처리
        if (!searchCriteria.isEmpty()) {
            // 특정 필드에 대해 검색 기준이 지정된 경우
            switch (searchCriteria) {
                case "couponId":
                    predicates.add(cb.equal(coupon.get("couponId"), safeParseInt(searchKeyword)));
                    break;
                case "couponTitle":
                    predicates.add(cb.like(coupon.get("couponTitle"), "%" + searchKeyword + "%"));
                    break;
                case "userId":
                    predicates.add(cb.like(userJoin.get("userId"), "%" + searchKeyword + "%"));
                    break;
                case "couponStatus":
                    predicates.add(cb.like(coupon.get("couponStatus"), "%" + searchKeyword + "%"));
                    break;
                case "couponType":
                	predicates.add(cb.like(coupon.get("couponType"), "%" + searchKeyword + "%"));
                    break;
                case "couponPeriod":
                	predicates.add(cb.like(cb.function("DATE_FORMAT", String.class, coupon.get("couponPeriod"), cb.literal("%Y-%m-%d")), "%" + searchKeyword + "%"));
                    break;
                default:
                    predicates.add(cb.conjunction());
            }
        } else {    	        	
            predicates.add(cb.or(
                cb.like(userJoin.get("userId"), "%" + searchKeyword + "%"),
                cb.like(coupon.get("couponTitle"), "%" + searchKeyword + "%"),
                cb.like(coupon.get("couponStatus"), "%" + searchKeyword + "%"),
                cb.like(coupon.get("couponType"), "%" + searchKeyword + "%"),
                cb.like(cb.function("DATE_FORMAT", String.class, coupon.get("couponPeriod"), cb.literal("%Y-%m-%d")), "%" + searchKeyword + "%"),
                cb.like(cb.concat(coupon.get("couponId").as(String.class), ""), "%" + searchKeyword + "%")
            ));
        }               

        // 필터 조건 처리
        for (Map<String, String> filterMap : filter) {
            String key = filterMap.get("type");
            String value = filterMap.get("value");
            if(key != null) {
	            switch (key) {
	                case "couponType":
	                	predicates.add(cb.like(coupon.get("couponType"), "%" + value));
	                    break;
	                case "couponStatus":
	                    predicates.add(cb.equal(coupon.get("couponStatus"), value));
	                    break;
	                case "date":
	                    String[] dateRange = value.split("~");
	                    if (dateRange.length == 2) {
	                        Timestamp startDate = Timestamp.valueOf(dateRange[0].trim() + " 00:00:00");
	                        Timestamp endDate = Timestamp.valueOf(dateRange[1].trim() + " 23:59:59");
	
	                        predicates.add(
                                cb.and(
                                    cb.greaterThanOrEqualTo(coupon.get("couponPeriod"), startDate),
                                    cb.lessThanOrEqualTo(coupon.get("couponPeriod"), endDate)
                                )
                            );
	                    }
	                    break;
	                default:
	                    break;
	            }
            }
        }
        query.select(coupon).where(cb.and(predicates.toArray(new Predicate[0])));       
        // 페이징 처리 및 쿼리 실행
        List<CouponEntity> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();       
        
        List<CouponEntity> resultListto = entityManager.createQuery(query)
              .getResultList();
        
        // 전체 개수 조회
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<CouponEntity> countRoot = countQuery.from(CouponEntity.class);
//        countRoot.join("eventManager", JoinType.LEFT);
        countQuery.select(cb.count(countRoot)).where(cb.and(predicates.toArray(new Predicate[0])));
        long total = resultListto.size();
        return new PageImpl<>(resultList, pageable, total);
	}
	
	private Integer safeParseInt(String keyword) {
        try {
            return Integer.parseInt(keyword);
        } catch (NumberFormatException e) {
            return null;
        }
    }
	
}
