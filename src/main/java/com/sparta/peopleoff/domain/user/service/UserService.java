package com.sparta.peopleoff.domain.user.service;

import com.sparta.peopleoff.domain.user.dto.UserChangePasswordDto;
import com.sparta.peopleoff.domain.user.dto.UserInfoResponseDto;
import com.sparta.peopleoff.domain.user.dto.UserSignUpRequestDto;
import com.sparta.peopleoff.domain.user.dto.UserUpdateRequestDto;
import com.sparta.peopleoff.security.UserDetailsImpl;

public interface UserService {

  void signUp(UserSignUpRequestDto userSignUpRequestDto);

  UserInfoResponseDto getUserInfo(UserDetailsImpl user);

  void deleteUser(Long userId);

  void updateUserInfo(Long userId, UserUpdateRequestDto userUpdateInfoDto);

  void changePassword(UserChangePasswordDto userChangePasswordDto,
      UserDetailsImpl userDetails);

  void applyManager(Long userId);
}
