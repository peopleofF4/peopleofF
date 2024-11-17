package com.sparta.peopleoff.domain.admin.service;

import com.sparta.peopleoff.domain.admin.dto.ManagerApproveRequestDto;
import com.sparta.peopleoff.domain.admin.dto.StoreApproveRequestDto;
import com.sparta.peopleoff.domain.admin.dto.UserResponseDto;
import com.sparta.peopleoff.domain.admin.dto.UserRoleRequestDto;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface AdminService {

  List<UserResponseDto> getUsers(String userName, Pageable pageable);

  void managerApprove(Long userId, ManagerApproveRequestDto managerApproveRequestDto);

  //List<UserResponseDto> searchUser(String userName);

  void updateUserRole(Long userId, UserRoleRequestDto userRoleRequestDto);

  void updateStoreRegist(UserEntity user, UUID storeId,
      StoreApproveRequestDto storeApproveRequestDto);

  void updateStoreDelete(UUID storeId, StoreApproveRequestDto storeApproveRequestDto);

}
