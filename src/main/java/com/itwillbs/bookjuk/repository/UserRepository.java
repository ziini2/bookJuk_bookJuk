package com.itwillbs.bookjuk.repository;


import com.itwillbs.bookjuk.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUserId(String userId);
    boolean existsByUserEmail(String email);
    boolean existsByUserPhone(String userPhone);
}
