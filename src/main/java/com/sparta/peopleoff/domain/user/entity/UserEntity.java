package com.sparta.peopleoff.domain.user.entity;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.domain.user.dto.UserSignUpRequestDto;
import com.sparta.peopleoff.domain.user.entity.enums.ManagerApproveRegistrationStatus;
import com.sparta.peopleoff.domain.user.entity.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  @Column(nullable = false, length = 50)
  private String phoneNumber;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Setter
  private UserRole role = UserRole.CUSTOMER; // 기본값 설정

  @Column(nullable = false, length = 255)
  private String address;

  @Column
  @Enumerated(EnumType.STRING)
  private ManagerApproveRegistrationStatus managerRegistrationStatus = ManagerApproveRegistrationStatus.NONE;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Setter
  private DeletionStatus deletionStatus = DeletionStatus.ACTIVE;

  public UserEntity(UserSignUpRequestDto userSignUpRequestDto) {
    this.userName = userSignUpRequestDto.getUserName();
    this.password = userSignUpRequestDto.getPassword();
    this.nickName = userSignUpRequestDto.getNickName();
    this.email = userSignUpRequestDto.getEmail();
    this.phoneNumber = userSignUpRequestDto.getPhoneNumber();
    this.address = userSignUpRequestDto.getAddress();
  }

}
