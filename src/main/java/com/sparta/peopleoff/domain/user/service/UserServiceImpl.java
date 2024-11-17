package com.sparta.peopleoff.domain.user.service;

import com.sparta.peopleoff.common.auditing.AuditorContext;
import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.user.dto.UserChangePasswordDto;
import com.sparta.peopleoff.domain.user.dto.UserInfoResponseDto;
import com.sparta.peopleoff.domain.user.dto.UserSignUpRequestDto;
import com.sparta.peopleoff.domain.user.dto.UserUpdateRequestDto;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.entity.enums.RegistrationStatus;
import com.sparta.peopleoff.domain.user.repository.UserRepository;
import com.sparta.peopleoff.exception.CustomApiException;
import com.sparta.peopleoff.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void signUp(UserSignUpRequestDto userSignUpRequestDto) {

    // Password Encode
    String password = passwordEncoder.encode(userSignUpRequestDto.getPassword());
    userSignUpRequestDto.setPassword(password);

    // 이메일 중복 여부
    checkDuplicateEmail(userSignUpRequestDto.getEmail());

    // 휴대폰 번호 중복 여부
    checkDuplicatePhoneNumber(userSignUpRequestDto.getPhoneNumber());

    // 닉네임 중복 여부
    checkDuplicateNickName(userSignUpRequestDto.getNickName());

    AuditorContext.setCurrentEmail(userSignUpRequestDto.getEmail());

    userRepository.save(new UserEntity(userSignUpRequestDto));

  }

  @Override
  public UserInfoResponseDto getUserInfo(UserDetailsImpl user) {
    UserInfoResponseDto response = new UserInfoResponseDto(user);
    return response;
  }

  @Override
  @Transactional
  public void deleteUser(Long userId) {

    UserEntity user = userRepository.findById(userId).orElseThrow(
        () -> new CustomApiException(ResBasicCode.NULL_POINT, "User Not Found.")
    );

    user.setDeletionStatus(DeletionStatus.DELETED);

  }

  @Override
  @Transactional
  public void updateUserInfo(Long userId, UserUpdateRequestDto userUpdateInfoDto) {

    UserEntity user = userRepository.findById(userId).orElseThrow(
        () -> new CustomApiException(ResBasicCode.NULL_POINT, "User Not Found.")
    );

    // 비밀번호 비교
    if (!passwordEncoder.matches(userUpdateInfoDto.getPassword(), user.getPassword())) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "wrong password");
    }

    // 휴대폰 번호 중복 여부
    checkDuplicatePhoneNumber(userUpdateInfoDto.getPhoneNumber());

    // 닉네임 중복 여부
    checkDuplicateNickName(userUpdateInfoDto.getNickName());

    user.updateUser(userUpdateInfoDto); // 여기도 변경감지

  }

  @Override
  @Transactional
  public void changePassword(UserChangePasswordDto userChangePasswordDto,
      UserDetailsImpl userDetails) {

    UserEntity user = userDetails.getUser();

    // 비밀번호 비교
    if (!passwordEncoder.matches(userChangePasswordDto.getPassword(), user.getPassword())) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "wrong password");
    }

    if (passwordEncoder.matches(userChangePasswordDto.getNewPassword(), user.getPassword())) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "same password");
    }

    String newPassword = passwordEncoder.encode(userChangePasswordDto.getNewPassword());

    user.updatePassword(newPassword);
    userRepository.save(user);

  }

  @Override
  @Transactional
  public void applyManager(Long userId) {

    UserEntity user = userRepository.findById(userId).orElseThrow(
        () -> new CustomApiException(ResBasicCode.NULL_POINT, "User Not Found.")
    );

    // 이미 Manager 신청이 됬거나 신청이 거부당한 User
    if (user.getManagerRegistrationStatus() != (RegistrationStatus.NONE)) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST,
          "manager already registered or rejected");
    }

    user.setManagerRegistrationStatus(RegistrationStatus.PENDING);

  }

  // 이메일 중복 체크 메서드
  private void checkDuplicateEmail(String email) {

    userRepository.findByEmail(email)
        .ifPresent(user -> {
          throw new CustomApiException(ResBasicCode.BAD_REQUEST, "중복된 Email이 존재합니다.");
        });
  }

  // 휴대폰번호 중복 체크 메서드
  private void checkDuplicatePhoneNumber(String phoneNumber) {

    userRepository.findByPhoneNumber(phoneNumber)
        .ifPresent(user -> {
          throw new CustomApiException(ResBasicCode.BAD_REQUEST, "중복된 PhoneNumber가 존재합니다.");
        });

  }

  // 닉네임 중복 체크 메서드
  private void checkDuplicateNickName(String nickName) {

    userRepository.findByNickName(nickName)
        .ifPresent(user -> {
          throw new CustomApiException(ResBasicCode.BAD_REQUEST, "중복된 NickName이 존재합니다.");
        });
  }


}
