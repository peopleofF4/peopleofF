package com.sparta.peopleoff.domain.user.repository;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByEmailAndDeletionStatus(String email, DeletionStatus deletionStatus);

  Optional<UserEntity> findByPhoneNumber(String phoneNumber);

  Optional<UserEntity> findByNickName(String nickName);
}
