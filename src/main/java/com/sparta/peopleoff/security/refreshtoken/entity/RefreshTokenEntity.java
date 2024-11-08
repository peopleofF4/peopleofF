package com.sparta.peopleoff.security.refreshtoken.entity;

import com.sparta.peopleoff.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_refresh_token")
@Getter
@NoArgsConstructor
public class RefreshTokenEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToOne
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;

  @Column(length = 255)
  private String refreshToken;

}
