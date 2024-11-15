package com.itwillbs.bookjuk.repository;


import com.itwillbs.bookjuk.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserNameAndUserEmail(String userName, String userEmail);
    Optional<UserEntity> findByUserIdAndUserPhone(String userId, String userPhone);
    UserEntity findByUserNum(Long userNum);
    UserEntity findByUserId(String userId);
    UserEntity findByUserEmail(String email);
    boolean existsByUserId(String userId);
    boolean existsByUserEmail(String userEmail);
    boolean existsByUserPhone(String userPhone);

    // dashboard 사용할 메서드
    long countByCreateDateBetween(Timestamp startOfDay, Timestamp endOfDay);

    Optional<List<UserEntity>> findAllByUserNameContaining(String keyword);

    Optional<List<UserEntity>> findByUserPhoneContaining(String keyword);
}
