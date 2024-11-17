package com.sparta.peopleoff.domain.user.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResSuccessCode;
import com.sparta.peopleoff.domain.user.dto.ManagerApproveRequestDto;
import com.sparta.peopleoff.domain.user.dto.UserResponseDto;
import com.sparta.peopleoff.domain.user.dto.UserRoleRequestDto;
import com.sparta.peopleoff.domain.user.service.AdminService;
import com.sparta.peopleoff.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/v1")
public class AdminController {

    private AdminService adminService;

    /**
     * 회원 전체 조회
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserResponseDto>>> getUsers (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<UserResponseDto> userResponseDtos = adminService.getUsers(PageRequest.of(page, size));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(userResponseDtos));
    }

    /**
     * 매니저 등록 승인
     *
     * @param userId
     * @param managerApproveRequestDto
     * @return
     */
    @PreAuthorize("hasAuthority('MASTER')")
    @PutMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> ManagerApprove(
            @PathVariable Long userId,
            @RequestBody ManagerApproveRequestDto managerApproveRequestDto
            ) {
        adminService.managerApprove(userId, managerApproveRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(ResSuccessCode.MANAGER_APPROVE));
    }

    /**
     * 회원 삭제
     * @param userId
     * @return
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Long>> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(userId));
    }

    /**
     * 유저 검색
     * @param userName
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/users/search")
    public ResponseEntity<ApiResponse<Page<UserResponseDto>>> searchUser(
            @RequestParam String userName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<UserResponseDto> responseDtos = adminService.searchUser(userName, PageRequest.of(page, size));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(responseDtos));
    }

    /**
     * 유저 권한 수정
     * @param userId
     * @return
     */
    @PutMapping("/users/{userId}/role/update")
    public ResponseEntity<ApiResponse<Void>> updateUserRole(@PathVariable Long userId,
                                                                          @RequestBody UserRoleRequestDto userRoleRequestDto
    ) {
        adminService.updateUserRole(userId, userRoleRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(ResSuccessCode.USER_ROLE_UPDATED));
    }

    /**
     * 가게 등록 승인 / 거부
     * @param userDetails
     * @param storeId
     * @param managerApproveRequestDto
     * @return
     */
    @PutMapping("/stores/{storeId}/regist")
    public ResponseEntity<ApiResponse<Void>> updateStoreRegist(@PathVariable UUID storeId,
                                                               @RequestBody ManagerApproveRequestDto managerApproveRequestDto) {
        adminService.updateStoreRegist(storeId, managerApproveRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(ResSuccessCode.STORE_REGISTRATION_UPDTAED));
    }

    /**
     * 가게 삭제 승인 / 거부
     * @param storeId
     * @param managerApproveRequestDto
     * @return
     */
    @PutMapping("/stores/{storeId}/delete")
    public ResponseEntity<ApiResponse<Void>> updateStoreDelete(@PathVariable UUID storeId,
                                                                @RequestBody ManagerApproveRequestDto managerApproveRequestDto) {
        adminService.updateStoreDelete(storeId, managerApproveRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(ResSuccessCode.STORE_DELETION_UPDTAED));
    }
}
