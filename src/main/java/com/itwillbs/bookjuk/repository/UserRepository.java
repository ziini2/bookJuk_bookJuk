package com.itwillbs.bookjuk.repository;


import com.itwillbs.bookjuk.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserId(String userId);
    UserEntity findByUserEmail(String email);
    boolean existsByUserId(String userId);
    boolean existsByUserEmail(String userEmail);
    boolean existsByUserPhone(String userPhone);
}
