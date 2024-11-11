package com.sparta.peopleoff.security.refreshtoken.entity;

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
public class RefreshTokenEntity {

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

  // TODO setter 만들어야 되는지?
  public void updateRefreshToken(String refreshToken, UserEntity user) {
    setRefreshToken(refreshToken);
    this.user = user;
  }

}
