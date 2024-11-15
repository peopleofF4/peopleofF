package com.sparta.peopleoff.jwt.refreshtoken.repository;

import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.jwt.refreshtoken.entity.RefreshTokenEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

  Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

  Optional<RefreshTokenEntity> findByUser(UserEntity user);
}
