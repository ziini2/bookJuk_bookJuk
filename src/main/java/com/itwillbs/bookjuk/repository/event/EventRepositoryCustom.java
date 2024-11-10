package com.itwillbs.bookjuk.repository.event;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itwillbs.bookjuk.entity.event.EventEntity;

public interface EventRepositoryCustom {
	Page<EventEntity> findByCriteriaAndFilter(String searchCriteria, 
										  String searchKeyword, 
										  List<Map<String, String>> filter, 
										  Pageable pageable, 
										  String sortColumn, 
										  String sortDirection);
}
