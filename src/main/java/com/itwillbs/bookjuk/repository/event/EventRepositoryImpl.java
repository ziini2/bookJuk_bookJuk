package com.itwillbs.bookjuk.repository.event;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.itwillbs.bookjuk.entity.event.EventEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class EventRepositoryImpl implements EventRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Page<EventEntity> findByCriteriaAndFilter(String searchCriteria, 
												 String searchKeyword, 
												 List<Map<String, String>> filter, 
												 Pageable pageable, 
												 String sortColumn, 
												 String sortDirection) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventEntity> query = cb.createQuery(EventEntity.class);
        Root<EventEntity> event = query.from(EventEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        // 검색 조건 처리
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            Predicate searchPredicate;

            if (searchCriteria != null && !searchCriteria.isEmpty()) {
                // 특정 필드에 대해 검색 기준이 지정된 경우
                switch (searchCriteria) {
                    case "eventId":
                    	try {
                            searchPredicate = cb.equal(event.get("eventId"), Integer.parseInt(searchKeyword));
                        } catch (NumberFormatException e) {
                            searchPredicate = cb.conjunction();
                        }
                        break;
                    case "eventTitle":
                        searchPredicate = cb.like(event.get("eventTitle"), "%" + searchKeyword + "%");
                        break;
                    case "eventType":
                        searchPredicate = cb.like(event.get("eventType"), "%" + searchKeyword + "%");
                        break;
                    case "eventStatus":
                        searchPredicate = cb.like(event.get("eventStatus"), "%" + searchKeyword + "%");
                        break;
                    case "eventManager":
                        searchPredicate = cb.like(event.get("eventManager").get("userName"), "%" + searchKeyword + "%");
                        break;
                    default:
                        searchPredicate = cb.conjunction(); // 기본값으로 무조건 참인 조건
                }
            } else {
                // 검색 기준이 없는 경우 (전체 검색)
                searchPredicate = cb.or(
                    cb.like(event.get("eventTitle"), "%" + searchKeyword + "%"),
                    cb.like(event.get("eventType"), "%" + searchKeyword + "%"),
                    cb.like(event.get("eventStatus"), "%" + searchKeyword + "%"),
                    cb.like(event.get("eventManager").get("userName"), "%" + searchKeyword + "%"),
                    cb.equal(event.get("eventId"), Integer.parseInt(searchKeyword))
                );
            }

            // 최종 생성된 검색 조건을 predicates에 추가
            predicates.add(searchPredicate);
        }

        // 필터 조건 처리
        for (Map<String, String> filterMap : filter) {
            String key = filterMap.get("key");
            String value = filterMap.get("value");

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

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        String sortField = (sortColumn != null && !sortColumn.isEmpty()) ? sortColumn : "eventId";
        String direction = (sortDirection != null && !sortDirection.isEmpty()) ? sortDirection : "desc";
        // 동적 정렬 설정
        if ("desc".equalsIgnoreCase(direction)) {
            query.orderBy(cb.desc(event.get(sortField)));
        } else {
            query.orderBy(cb.asc(event.get(sortField)));
        }

        // 페이징 처리 및 쿼리 실행
        List<EventEntity> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // 전체 개수 조회
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<EventEntity> countRoot = countQuery.from(EventEntity.class);
        countQuery.select(cb.count(countRoot)).where(cb.and(predicates.toArray(new Predicate[0])));
        long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
	}
	
}
