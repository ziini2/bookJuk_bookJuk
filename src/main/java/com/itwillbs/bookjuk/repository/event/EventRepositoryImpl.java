package com.itwillbs.bookjuk.repository.event;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.event.EventEntity;

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
public class EventRepositoryImpl implements EventRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Page<EventEntity> findByCriteriaAndFilter(String searchCriteria, 
												 String searchKeyword, 
												 List<Map<String, String>> filter, 
												 Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventEntity> query = cb.createQuery(EventEntity.class);
        Root<EventEntity> event = query.from(EventEntity.class);
        Join<EventEntity, UserEntity> eventManagerJoin = event.join("eventManager", JoinType.LEFT);
        List<Predicate> predicates = new ArrayList<>();
        // 검색 조건 처리
        if (!searchCriteria.isEmpty()) {
            // 특정 필드에 대해 검색 기준이 지정된 경우
            switch (searchCriteria) {
                case "eventId":
                    predicates.add(cb.equal(event.get("eventId"), safeParseInt(searchKeyword)));
                    break;
                case "eventTitle":
                    predicates.add(cb.like(event.get("eventTitle"), "%" + searchKeyword + "%"));
                    break;
                case "eventType":
                    predicates.add(cb.like(event.get("eventType"), "%" + searchKeyword + "%"));
                    break;
                case "eventStatus":
                    predicates.add(cb.like(event.get("eventStatus"), "%" + searchKeyword + "%"));
                    break;
                case "eventManager":
                	predicates.add(cb.like(eventManagerJoin.get("userName"), "%" + searchKeyword + "%"));
                    break;
                default:
                    predicates.add(cb.conjunction());
            }
        } else {    	        	
            predicates.add(cb.or(
                cb.like(event.get("eventTitle"), "%" + searchKeyword + "%"),
                cb.like(event.get("eventType"), "%" + searchKeyword + "%"),
                cb.like(event.get("eventStatus"), "%" + searchKeyword + "%"),
                cb.like(eventManagerJoin.get("userName"), "%" + searchKeyword + "%"),
                cb.like(cb.concat(event.get("eventId").as(String.class), ""), "%" + searchKeyword + "%")
            ));
        }               

        // 필터 조건 처리
        for (Map<String, String> filterMap : filter) {
            String key = filterMap.get("type");
            String value = filterMap.get("value");
            if(key != null) {
	            switch (key) {
	                case "eventType":
	                    predicates.add(cb.equal(event.get("eventType"), value));
	                    break;
	                case "eventStatus":
	                    predicates.add(cb.equal(event.get("eventStatus"), value));
	                    break;
	                case "date":
	                    String[] dateRange = value.split("~");
	                    if (dateRange.length == 2) {
	                        Timestamp startDate = Timestamp.valueOf(dateRange[0].trim() + " 00:00:00");
	                        Timestamp endDate = Timestamp.valueOf(dateRange[1].trim() + " 23:59:59");
	
	                        predicates.add(cb.or(
	                            cb.and(
	                                cb.greaterThanOrEqualTo(event.get("endEventDate"), startDate),
	                                cb.lessThanOrEqualTo(event.get("startEventDate"), endDate)
	                            ),
	                            cb.and(
	                                cb.lessThanOrEqualTo(event.get("startEventDate"), endDate),
	                                cb.greaterThanOrEqualTo(event.get("endEventDate"), startDate)
	                            )
	                        ));
	                    }
	                    break;
	                default:
	                    break;
	            }
            }
        }
        query.select(event).where(cb.and(predicates.toArray(new Predicate[0])));       
        // 페이징 처리 및 쿼리 실행
        List<EventEntity> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();       
        
        List<EventEntity> resultListto = entityManager.createQuery(query)
              .getResultList();
        
        // 전체 개수 조회
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<EventEntity> countRoot = countQuery.from(EventEntity.class);
        countRoot.join("eventManager", JoinType.LEFT);
        countQuery.select(cb.count(countRoot)).where(cb.and(predicates.toArray(new Predicate[0])));
//        long total = entityManager.createQuery(countQuery).getSingleResult();
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
