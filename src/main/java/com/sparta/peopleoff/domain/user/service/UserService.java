package com.sparta.peopleoff.domain.user.service;

import com.sparta.peopleoff.common.auditing.AuditorContext;
import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.user.dto.UserChangePasswordDto;
import com.sparta.peopleoff.domain.user.dto.UserInfoResponseDto;
import com.sparta.peopleoff.domain.user.dto.UserSignUpRequestDto;
import com.sparta.peopleoff.domain.user.dto.UserUpdateRequestDto;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.repository.UserRepository;
import com.sparta.peopleoff.exception.CustomApiException;
import com.sparta.peopleoff.security.UserDetailsImpl;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void signUp(UserSignUpRequestDto userSignUpRequestDto) {

    // Password Encode
    String password = passwordEncoder.encode(userSignUpRequestDto.getPassword());
    userSignUpRequestDto.setPassword(password);

    // 이메일 중복 여부
    Optional.ofNullable(userRepository.findByEmail(userSignUpRequestDto.getEmail()))
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "중복된 Email이 존재합니다."));

    // 휴대폰 번호 중복 여부
    Optional.ofNullable(userRepository.findByPhoneNumber(userSignUpRequestDto.getPhoneNumber()))
        .orElseThrow(
            () -> new CustomApiException(ResBasicCode.BAD_REQUEST, "중복된 PhoneNumber가 존재합니다."));
    // 닉네임 중복 여부
    Optional.ofNullable(userRepository.findByNickName(userSignUpRequestDto.getNickName()))
        .orElseThrow(
            () -> new CustomApiException(ResBasicCode.BAD_REQUEST, "중복된 NickName이 존재합니다."));

    AuditorContext.setCurrentEmail(userSignUpRequestDto.getEmail());

    userRepository.save(new UserEntity(userSignUpRequestDto));

  }

  public UserInfoResponseDto getUserInfo(UserDetailsImpl user) {
    UserInfoResponseDto response = new UserInfoResponseDto(user);
    return response;
  }

  @Transactional
  public void deleteUser(Long userId) {

    UserEntity user = userRepository.findById(userId).orElseThrow(
        () -> new CustomApiException(ResBasicCode.NULL_POINT, "User Not Found.")
    );

    user.setDeletionStatus(DeletionStatus.DELETED);

  }

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
    Optional.ofNullable(userRepository.findByPhoneNumber(userUpdateInfoDto.getPhoneNumber()))
        .orElseThrow(
            () -> new CustomApiException(ResBasicCode.BAD_REQUEST, "중복된 PhoneNumber가 존재합니다."));
    // 닉네임 중복 여부
    Optional.ofNullable(userRepository.findByNickName(userUpdateInfoDto.getNickName()))
        .orElseThrow(
            () -> new CustomApiException(ResBasicCode.BAD_REQUEST, "중복된 NickName이 존재합니다."));

    user.updateUser(userUpdateInfoDto); // 여기도 변경감지 일어나나 check

  }

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
}
