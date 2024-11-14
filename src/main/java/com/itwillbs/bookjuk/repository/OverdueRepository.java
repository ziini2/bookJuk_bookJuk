package com.itwillbs.bookjuk.repository;

import com.itwillbs.bookjuk.entity.rent.Overdue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OverdueRepository extends JpaRepository<Overdue, Long> {

}
