package com.sparta.peopleoff.domain.store.controller;

import com.sparta.peopleoff.domain.store.dto.StorePostRequestDto;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.store.service.StoreService;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stores")
public class StoreController {

  private final StoreService storeService;
  private final UserService userService;

  public StoreController(StoreService storeService, UserService userService) {
    this.storeService = storeService;
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<?> registerStore(@RequestBody StorePostRequestDto storeRequestDto) {

    // UserEntity user = userService.getAuthenticatedUser();

    // 테스트용
    UserEntity user = userService.findById(storeRequestDto.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));

    if (!user.getRole().equals("OWNER")) {
      return ResponseEntity.status(403).body("권한이 없습니다.");
    }

    StoreEntity store = storeService.registerStore(storeRequestDto, user);

    return ResponseEntity.ok().build();
  }
}
