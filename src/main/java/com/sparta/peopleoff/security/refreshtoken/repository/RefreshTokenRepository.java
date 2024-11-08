package com.sparta.peopleoff.security.refreshtoken.repository;

import com.sparta.peopleoff.security.refreshtoken.entity.RefreshTokenEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

}
