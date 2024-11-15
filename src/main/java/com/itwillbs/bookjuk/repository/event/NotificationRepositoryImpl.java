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
import com.itwillbs.bookjuk.entity.event.NotificationEntity;

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
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Page<NotificationEntity> notiTable(Long userNum,
											  String searchCriteria, 
											  String searchKeyword, 
										 	  List<Map<String, String>> filter, 
											  Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<NotificationEntity> query = cb.createQuery(NotificationEntity.class);
        Root<NotificationEntity> noti = query.from(NotificationEntity.class);
        Join<NotificationEntity, UserEntity> recipientJoin = noti.join("notiRecipient", JoinType.LEFT);
        Join<NotificationEntity, UserEntity> senderJoin = noti.join("notiSender", JoinType.LEFT);
        List<Predicate> predicates = new ArrayList<>();
        // 회원으로 접속할 경우 회원 아이디 조회 추가
        if (userNum != 0L) {
            predicates.add(cb.equal(recipientJoin.get("userNum"), userNum));
        }
        // 검색 조건 처리
        if (!searchCriteria.isEmpty()) {
            // 특정 필드에 대해 검색 기준이 지정된 경우
            switch (searchCriteria) {
                case "notiId":
                    predicates.add(cb.like(cb.concat(noti.get("notiId").as(String.class), ""), "%" + searchKeyword + "%"));
                    break;
                case "recipient":
                    predicates.add(cb.like(recipientJoin.get("userId"), "%" + searchKeyword + "%"));
                    break;
                case "sender":
                    predicates.add(cb.like(senderJoin.get("userName"), "%" + searchKeyword + "%"));
                    break;
                case "notiContent":
                    predicates.add(cb.like(noti.get("notiContent"), "%" + searchKeyword + "%"));
                    break;
                case "notiType":
                    predicates.add(cb.like(noti.get("notiType"), "%" + searchKeyword + "%"));
                    break;
                case "notiStatus":
                	predicates.add(cb.like(noti.get("notiStatus"), "%" + searchKeyword + "%"));
                    break;
                case "notiSentDate":
                	predicates.add(cb.like(cb.function("DATE_FORMAT", String.class, noti.get("notiSentDate"), cb.literal("%Y-%m-%d")), "%" + searchKeyword + "%"));
                    break;
                default:
                    predicates.add(cb.conjunction());
            }
        } else {   
        	if(userNum != 0L) {
        		predicates.add(cb.or(
		            cb.like(senderJoin.get("userName"), "%" + searchKeyword + "%"),
		            cb.like(noti.get("notiContent"), "%" + searchKeyword + "%"),
		            cb.like(noti.get("notiType"), "%" + searchKeyword + "%"),
		            cb.like(noti.get("notiStatus"), "%" + searchKeyword + "%"),
		            cb.like(cb.function("DATE_FORMAT", String.class, noti.get("notiSentDate"), cb.literal("%Y-%m-%d")), "%" + searchKeyword + "%"),
		            cb.like(cb.concat(noti.get("notiId").as(String.class), ""), "%" + searchKeyword + "%")
		        ));
        	}else {
        		predicates.add(cb.or(
		            cb.like(recipientJoin.get("userId"), "%" + searchKeyword + "%"),
		            cb.like(noti.get("notiContent"), "%" + searchKeyword + "%"),
		            cb.like(noti.get("notiType"), "%" + searchKeyword + "%"),
		            cb.like(noti.get("notiStatus"), "%" + searchKeyword + "%"),
		            cb.like(cb.function("DATE_FORMAT", String.class, noti.get("notiSentDate"), cb.literal("%Y-%m-%d")), "%" + searchKeyword + "%"),
		            cb.like(cb.concat(noti.get("notiId").as(String.class), ""), "%" + searchKeyword + "%")
		        ));
        	}
        }               

        // 필터 조건 처리
        for (Map<String, String> filterMap : filter) {
            String key = filterMap.get("type");
            String value = filterMap.get("value");
            if(key != null) {
	            switch (key) {
	                case "notiType":
	                    predicates.add(cb.equal(noti.get("notiType"), value));
	                    break;
	                case "notiStatus":
	                    predicates.add(cb.equal(noti.get("notiStatus"), value));
	                    break;
	                case "date":
	                    String[] dateRange = value.split("~");
	                    if (dateRange.length == 2) {
	                        Timestamp startDate = Timestamp.valueOf(dateRange[0].trim() + " 00:00:00");
	                        Timestamp endDate = Timestamp.valueOf(dateRange[1].trim() + " 23:59:59");
	
	                        predicates.add(
                                cb.and(
                                    cb.greaterThanOrEqualTo(noti.get("notiSentDate"), startDate),
                                    cb.lessThanOrEqualTo(noti.get("notiSentDate"), endDate)
                                )
                            );
	                    }
	                    break;
	                default:
	                    break;
	            }
            }
        }
        query.select(noti).where(cb.and(predicates.toArray(new Predicate[0])));       
        // 페이징 처리 및 쿼리 실행
        List<NotificationEntity> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();       

        // 필터링 된 데이터 개수 조회
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<NotificationEntity> countRoot = countQuery.from(NotificationEntity.class);
        countRoot.join("notiRecipient", JoinType.LEFT);
        countRoot.join("notiSender", JoinType.LEFT);
        countQuery.select(cb.count(countRoot)).where(cb.and(predicates.toArray(new Predicate[0])));
        long total = entityManager.createQuery(countQuery).getSingleResult();
        return new PageImpl<>(resultList, pageable, total);
	}
}
