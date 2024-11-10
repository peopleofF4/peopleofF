package com.sparta.peopleoff.domain.user.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.domain.user.dto.ManagerApproveRequestDto;
import com.sparta.peopleoff.domain.user.dto.ManagerApproveResponseDto;
import com.sparta.peopleoff.domain.user.dto.UserResponseDto;
import com.sparta.peopleoff.domain.user.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/user")
public class AdminController {

    private AdminService adminService;

    /**
     * 회원 전체 조회
     * @param userDetails
     * @return
     */
//    @GetMapping
//    private ResponseEntity<ApiResponse<List<UserResponseDto>>> getUsers (
//            @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        List<UserResponseDto> userResponseDtos = adminService.getUsers(userDetails.getUser());
//        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(userResponseDtos));
//    }

    // 매니저 등록 승인
    @PutMapping("/{userId}")
    private ResponseEntity<ApiResponse<ManagerApproveResponseDto>> ManagerApprove() {
        ManagerApproveResponseDto managerApproveResponseDto = adminService.managerApprove();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(managerApproveResponseDto));
    }
}
