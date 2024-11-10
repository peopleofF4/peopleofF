package com.sparta.peopleoff.domain.user.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.domain.user.dto.UserResponseDto;
import com.sparta.peopleoff.domain.user.service.ManagerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/management")
public class ManagerController {

    private ManagerService managerService;

//    @PutMapping("/user/{userId}")
//   private ResponseEntity<ApiResponse<Long>> deleteUser(@PathVariable Long userId,
//                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        managerService.deleteUser(userId, userDetails.getUser());
//        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(userId));
//    }

    /**
     * 유저 검색
     * @param userName
     * @return
     */
    @GetMapping("/store/search")
    private ResponseEntity<ApiResponse<List<UserResponseDto>>> searchUser(
            @RequestParam String userName
//            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<UserResponseDto> responseDtos = managerService.searchUser(userName/*, userDetails*/);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(responseDtos));
    }
}
