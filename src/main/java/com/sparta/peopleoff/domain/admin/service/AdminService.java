package com.sparta.peopleoff.domain.admin.service;

import com.sparta.peopleoff.domain.admin.dto.ManagerApproveRequestDto;
import com.sparta.peopleoff.domain.admin.dto.UserResponseDto;
import com.sparta.peopleoff.domain.admin.dto.UserRoleRequestDto;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

  Page<UserResponseDto> getUsers(Pageable pageable);

  void managerApprove(Long userId, ManagerApproveRequestDto managerApproveRequestDto);

  List<UserResponseDto> searchUser(String userName);

  void updateUserRole(Long userId, UserRoleRequestDto userRoleRequestDto);

  void updateStoreRegist(UserEntity user, UUID storeId,
      ManagerApproveRequestDto managerApproveRequestDto);

  void updateStoreDelete(UUID storeId, ManagerApproveRequestDto managerApproveRequestDto);

}
