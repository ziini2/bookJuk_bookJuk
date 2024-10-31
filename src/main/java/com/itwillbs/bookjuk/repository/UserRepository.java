package com.itwillbs.bookjuk.repository;


import com.itwillbs.bookjuk.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
