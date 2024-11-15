package com.itwillbs.bookjuk.repository;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.entity.rent.RentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentRepository extends JpaRepository<RentEntity, Long> {
    Page<RentEntity> findAll(Pageable pageable);


    Page<RentEntity> findByUser(UserEntity user, Pageable pageable);


    Page<RentEntity> findAllByBookIn(List<BooksEntity> booksId, Pageable pageable);

    Page<RentEntity> findAllByUserIn(List<UserEntity> userEntity, Pageable pageable);

    Page<RentEntity> findAllByRentStatusIsTrue(Pageable pageable);

    Page<RentEntity> findAllByRentStatusIsFalse(Pageable pageable);

    Page<RentEntity> findAllByUserInAndRentStatusIsTrue(List<UserEntity> userEntities, Pageable pageable);

    Page<RentEntity> findAllByUserInAndRentStatusIsFalse(List<UserEntity> userEntities, Pageable pageable);

    Page<RentEntity> findAllByBookInAndRentStatusIsFalse(List<BooksEntity> booksEntities, Pageable pageable);

    Page<RentEntity> findAllByBookInAndRentStatusIsTrue(List<BooksEntity> booksEntities, Pageable pageable);

    Optional<Long> countByRentStatusIsFalse();

    Optional<Long> countByRentEndAfterAndRentStatusIsFalse(LocalDate localDate);

    //마이페이지 유저 rent 내역 가져오기
    Page<RentEntity> findByUser_UserNum(Long userNum, Pageable pageable);
}