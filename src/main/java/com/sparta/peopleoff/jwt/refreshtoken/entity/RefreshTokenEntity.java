package com.sparta.peopleoff.jwt.refreshtoken.entity;

import com.sparta.peopleoff.common.entity.SoftDeleteEntity;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_refresh_token")
@Getter
@NoArgsConstructor
public class RefreshTokenEntity extends SoftDeleteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(length = 255)
  private String refreshToken;

  private void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  // 외래키의 주인은 생성자로도 set 가능
  public void updateRefreshToken(String refreshToken, UserEntity user) {
    this.user = user;
    this.refreshToken = refreshToken;
  }

}
