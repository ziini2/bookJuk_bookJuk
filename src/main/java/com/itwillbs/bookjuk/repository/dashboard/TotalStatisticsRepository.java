package com.itwillbs.bookjuk.repository.dashboard;

import com.itwillbs.bookjuk.entity.rent.RentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TotalStatisticsRepository extends JpaRepository<RentEntity, Long> {


}
