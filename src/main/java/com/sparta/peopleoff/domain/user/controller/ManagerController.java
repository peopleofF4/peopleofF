package com.sparta.peopleoff.domain.user.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.domain.user.dto.ManagerApproveRequestDto;
import com.sparta.peopleoff.domain.user.dto.UpdateResponseDto;
import com.sparta.peopleoff.domain.user.dto.UserResponseDto;
import com.sparta.peopleoff.domain.user.dto.UserRoleRequestDto;
import com.sparta.peopleoff.domain.user.service.ManagerService;
import com.sparta.peopleoff.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/management")
public class ManagerController {

    private ManagerService managerService;

    /**
     * 회원 삭제
     * @param userId
     * @param userDetails
     * @return
     */
    @DeleteMapping("/user/{userId}")
   private ResponseEntity<ApiResponse<Long>> deleteUser(@PathVariable Long userId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        managerService.deleteUser(userId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(userId));
    }

    /**
     * 유저 검색
     * @param userName
     * @return
     */
    @GetMapping("/user/search")
    private ResponseEntity<ApiResponse<List<UserResponseDto>>> searchUser(
            @RequestParam String userName,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<UserResponseDto> responseDtos = managerService.searchUser(userName, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(responseDtos));
    }

    /**
     * 유저 권한 수정
     * @param userId
     * @return
     */
    @PutMapping("/user/{userId}/role/update")
    private ResponseEntity<ApiResponse<UpdateResponseDto>> updateUserRole(@PathVariable Long userId,
                                                                          @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                          @RequestBody UserRoleRequestDto userRoleRequestDto
    ) {
        UpdateResponseDto updateResponseDto = managerService.updateUserRole(userId, userDetails.getUser(), userRoleRequestDto);
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
        UpdateResponseDto updateResponseDto = managerService.updateStoreRegist(userDetails.getUser(), storeId, managerApproveRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(updateResponseDto));
    }

    /**
     * 가게 삭제 승인 / 거부
     * @param userDetails
     * @param storeId
     * @param managerApproveRequestDto
     * @return
     */ //TODO: 반환 타입 UpdateResponseDto로 바꾸기
    @PutMapping("/store/{storeId}/delete")
    private ResponseEntity<ApiResponse<UpdateResponseDto>> updateStoreDelete(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @PathVariable UUID storeId,
                                                                  @RequestBody ManagerApproveRequestDto managerApproveRequestDto) {
        UpdateResponseDto updateResponseDto = managerService.updateStoreDelete(userDetails.getUser(), storeId, managerApproveRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(updateResponseDto));
    }
}
