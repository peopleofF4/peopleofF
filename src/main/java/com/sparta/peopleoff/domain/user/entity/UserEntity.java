package com.sparta.peopleoff.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_user")
@Getter
@NoArgsConstructor
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String userName;

  @Column(nullable = false, length = 100)
  private String password;

  @Column(length = 100)
  private String nickName;

  @Column(nullable = false, unique = true, length = 255)
  private String email;

  @Column(nullable = false ,length = 50)
  private String phoneNumber;

  // Enum : CUSTOMER:고객 / OWNER:사장 / MANAGER:매니저 / MASTER:마스터
  private String role;

  @Column(nullable = false, length = 255)
  private String address;

  // Enum : active:활성 / deleted:삭제
  private String deletionStatus;

}
