package com.itwillbs.bookjuk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.UserEntity;

public interface MemberpageRepository extends JpaRepository<UserEntity, Long>{

	Optional<UserEntity> findByuserId(String userId);

}
