package com.sparta.peopleoff.domain.user.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResSuccessCode;
import com.sparta.peopleoff.domain.user.dto.ManagerApproveRequestDto;
import com.sparta.peopleoff.domain.user.dto.UpdateResponseDto;
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
@RequestMapping("/api/v1/admin")
public class AdminController {

    private AdminService adminService;

    /**
     * 회원 전체 조회
     * @return
     */
    @GetMapping("/user")
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
    @PutMapping("/user/{userId}")
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
    @DeleteMapping("/user/{userId}")
    private ResponseEntity<ApiResponse<Long>> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(userId));
    }

    /**
     * 유저 검색
     * @param userName
     * @return
     */
    @GetMapping("/user/search")
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
    @PutMapping("/user/{userId}/role/update")
    private ResponseEntity<ApiResponse<UpdateResponseDto>> updateUserRole(@PathVariable Long userId,
                                                                          @RequestBody UserRoleRequestDto userRoleRequestDto
    ) {
        UpdateResponseDto updateResponseDto = adminService.updateUserRole(userId, userRoleRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(updateResponseDto));
    }

    /**
     * 가게 등록 승인 / 거부
     * @param userDetails
     * @param storeId
     * @param managerApproveRequestDto
     * @return
     */
    @PutMapping("/store/{storeId}/regist")
    private ResponseEntity<ApiResponse<UpdateResponseDto>> updateStoreRegist(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                             @PathVariable UUID storeId,
                                                                             @RequestBody ManagerApproveRequestDto managerApproveRequestDto) {
        UpdateResponseDto updateResponseDto = adminService.updateStoreRegist(userDetails.getUser(), storeId, managerApproveRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(updateResponseDto));
    }

    /**
     * 가게 삭제 승인 / 거부
     * @param userDetails
     * @param storeId
     * @param managerApproveRequestDto
     * @return
     */
    @PutMapping("/store/{storeId}/delete")
    private ResponseEntity<ApiResponse<UpdateResponseDto>> updateStoreDelete(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                             @PathVariable UUID storeId,
                                                                             @RequestBody ManagerApproveRequestDto managerApproveRequestDto) {
        UpdateResponseDto updateResponseDto = adminService.updateStoreDelete(userDetails.getUser(), storeId, managerApproveRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(updateResponseDto));
    }
}
