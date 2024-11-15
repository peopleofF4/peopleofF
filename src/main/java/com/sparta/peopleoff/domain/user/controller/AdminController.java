package com.sparta.peopleoff.domain.user.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResSuccessCode;
import com.sparta.peopleoff.domain.user.dto.ManagerApproveRequestDto;
import com.sparta.peopleoff.domain.user.dto.UserResponseDto;
import com.sparta.peopleoff.domain.user.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/user")
public class AdminController {

    private AdminService adminService;

    /**
     * 회원 전체 조회
     * @return
     */
    @PreAuthorize("hasAuthority('MASTER') or hasAuthority('MANAGER')")
    @GetMapping
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
    @PutMapping("/{userId}")
    private ResponseEntity<ApiResponse<Void>> ManagerApprove(
            @PathVariable Long userId,
            @RequestBody ManagerApproveRequestDto managerApproveRequestDto
            ) {
        adminService.managerApprove(userId, managerApproveRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(ResSuccessCode.MANAGER_APPROVE));
    }
}
