package com.sparta.peopleoff.domain.user.service;

import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.user.dto.UserInfoResponseDto;
import com.sparta.peopleoff.domain.user.dto.UserSignUpRequestDto;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.repository.UserRepository;
import com.sparta.peopleoff.exception.CustomApiException;
import com.sparta.peopleoff.security.UserDetailsImpl;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void signUp(UserSignUpRequestDto userSignUpRequestDto) {

    // Password Encode
    String password = passwordEncoder.encode(userSignUpRequestDto.getPassword());
    userSignUpRequestDto.setPassword(password);

    System.out.println("password : " + password);

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

    userRepository.save(new UserEntity(userSignUpRequestDto));

  }

  public UserInfoResponseDto getUserInfo(UserDetailsImpl user) {
    UserInfoResponseDto response = new UserInfoResponseDto(user);
    return response;
  }
}
