package com.sparta.peopleoff.domain.admin.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResSuccessCode;
import com.sparta.peopleoff.domain.admin.dto.ManagerApproveRequestDto;
import com.sparta.peopleoff.domain.admin.dto.UserResponseDto;
import com.sparta.peopleoff.domain.admin.dto.UserRoleRequestDto;
import com.sparta.peopleoff.domain.admin.service.AdminServiceImpl;
import com.sparta.peopleoff.security.UserDetailsImpl;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1")
public class AdminController {

  private final AdminServiceImpl adminServiceImpl;

  /**
   * 회원 전체 조회
   *
   * @return
   */
  @GetMapping("/users")
  public ResponseEntity<ApiResponse<Page<UserResponseDto>>> getUsers(
      @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<UserResponseDto> userResponseDtos = adminServiceImpl.getUsers(pageable);
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(userResponseDtos));
  }

  /**
   * 매니저 등록 승인
   *
   * @param userId
   * @param managerApproveRequestDto
   * @return
   */
  @PreAuthorize("hasRole('MASTER')")
  @PutMapping("/users/{userId}")
  public ResponseEntity<ApiResponse<Void>> ManagerApprove(
      @PathVariable Long userId,
      @RequestBody ManagerApproveRequestDto managerApproveRequestDto
  ) {
    adminServiceImpl.managerApprove(userId, managerApproveRequestDto);

    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.OK(ResSuccessCode.MANAGER_APPROVE));
  }

  /**
   * 유저 검색
   *
   * @param userName
   * @return
   */
  @GetMapping("/users/search")
  public ResponseEntity<ApiResponse<List<UserResponseDto>>> searchUser(
      @RequestParam String userName
  ) {
    List<UserResponseDto> responseDtos = adminServiceImpl.searchUser(userName);
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(responseDtos));
  }

  /**
   * 유저 권한 수정
   *
   * @param userId
   * @return
   */
  @PutMapping("/users/{userId}/role/update")
  public ResponseEntity<ApiResponse<Void>> updateUserRole(@PathVariable Long userId,
      @RequestBody UserRoleRequestDto userRoleRequestDto
  ) {
    adminServiceImpl.updateUserRole(userId, userRoleRequestDto);
    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.OK(ResSuccessCode.USER_ROLE_UPDATED));
  }

  /**
   * 가게 등록 승인 / 거부
   *
   * @param userDetails
   * @param storeId
   * @param managerApproveRequestDto
   * @return
   */
  @PutMapping("/stores/{storeId}/regist")
  public ResponseEntity<ApiResponse<Void>> updateStoreRegist(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable UUID storeId,
      @RequestBody ManagerApproveRequestDto managerApproveRequestDto) {
    adminServiceImpl.updateStoreRegist(userDetails.getUser(), storeId, managerApproveRequestDto);
    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.OK(ResSuccessCode.STORE_REGISTRATION_UPDTAED));
  }

  /**
   * 가게 삭제 승인 / 거부
   *
   * @param storeId
   * @param managerApproveRequestDto
   * @return
   */
  @PutMapping("/stores/{storeId}/delete")
  public ResponseEntity<ApiResponse<Void>> updateStoreDelete(@PathVariable UUID storeId,
      @RequestBody ManagerApproveRequestDto managerApproveRequestDto) {
    adminServiceImpl.updateStoreDelete(storeId, managerApproveRequestDto);
    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.OK(ResSuccessCode.STORE_DELETION_UPDTAED));
  }
}
