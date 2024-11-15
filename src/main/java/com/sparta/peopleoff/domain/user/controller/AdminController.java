package com.sparta.peopleoff.domain.user.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResSuccessCode;
import com.sparta.peopleoff.domain.user.dto.ManagerApproveRequestDto;
import com.sparta.peopleoff.domain.user.dto.UserResponseDto;
import com.sparta.peopleoff.domain.user.dto.UserRoleRequestDto;
import com.sparta.peopleoff.domain.user.service.AdminService;
import com.sparta.peopleoff.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admins")
public class AdminController {

    private AdminService adminService;

    /**
     * 회원 전체 조회
     * @return
     */
    @GetMapping("/users")
    private ResponseEntity<ApiResponse<List<UserResponseDto>>> getUsers () {
        List<UserResponseDto> userResponseDtos = adminService.getUsers();
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
    private ResponseEntity<ApiResponse<Void>> ManagerApprove(
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
    private ResponseEntity<ApiResponse<Long>> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(userId));
    }

    /**
     * 유저 검색
     * @param userName
     * @return
     */
    @GetMapping("/users/search")
    private ResponseEntity<ApiResponse<List<UserResponseDto>>> searchUser(
            @RequestParam String userName
    ) {
        List<UserResponseDto> responseDtos = adminService.searchUser(userName);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(responseDtos));
    }

    /**
     * 유저 권한 수정
     * @param userId
     * @return
     */
    @PutMapping("/users/{userId}/role/update")
    private ResponseEntity<ApiResponse<Void>> updateUserRole(@PathVariable Long userId,
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
    private ResponseEntity<ApiResponse<Void>> updateStoreRegist(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                             @PathVariable UUID storeId,
                                                                             @RequestBody ManagerApproveRequestDto managerApproveRequestDto) {
        adminService.updateStoreRegist(userDetails.getUser(), storeId, managerApproveRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(ResSuccessCode.STORE_REGISTRATION_UPDTAED));
    }

    /**
     * 가게 삭제 승인 / 거부
     * @param userDetails
     * @param storeId
     * @param managerApproveRequestDto
     * @return
     */
    @PutMapping("/stores/{storeId}/delete")
    private ResponseEntity<ApiResponse<Void>> updateStoreDelete(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                             @PathVariable UUID storeId,
                                                                             @RequestBody ManagerApproveRequestDto managerApproveRequestDto) {
        adminService.updateStoreDelete(userDetails.getUser(), storeId, managerApproveRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(ResSuccessCode.STORE_DELETION_UPDTAED));
    }
}
