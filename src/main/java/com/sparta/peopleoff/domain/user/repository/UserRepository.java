package com.sparta.peopleoff.domain.user.repository;

import com.sparta.peopleoff.domain.user.dto.UserResponseDto;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByPhoneNumber(String phoneNumber);

  Optional<UserEntity> findByNickName(String nickName);

  List<UserEntity> findByUserNameContaining(String userName);
}
