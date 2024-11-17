package com.itwillbs.bookjuk.repository;

import com.itwillbs.bookjuk.entity.rent.Overdue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OverdueRepository extends JpaRepository<Overdue, Long> {

    Optional<List<Overdue>> findAllByOverStartBetween(LocalDate of, LocalDate of1);
}
