package com.sparta.peopleoff.domain.user.repository;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByEmailAndDeletionStatus(String email, DeletionStatus deletionStatus);

  Optional<UserEntity> findByPhoneNumber(String phoneNumber);

  Optional<UserEntity> findByNickName(String nickName);

  Page<UserEntity> findByUserNameContaining(String userName, Pageable pageable);
}
