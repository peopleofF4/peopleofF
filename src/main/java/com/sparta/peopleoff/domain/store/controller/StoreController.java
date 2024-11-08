package com.sparta.peopleoff.domain.store.controller;

import com.sparta.peopleoff.domain.store.dto.StoreGetResponseDto;
import com.sparta.peopleoff.domain.store.dto.StorePostRequestDto;
import com.sparta.peopleoff.domain.store.dto.StorePutRequestDto;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.store.service.StoreService;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.service.UserService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping("/{storeId}")
  public ResponseEntity<StoreGetResponseDto> getStoreById(@PathVariable UUID storeId) {
    StoreGetResponseDto store = storeService.getStoreById(storeId);
    return ResponseEntity.ok(store);
  }

  @GetMapping
  public ResponseEntity<List<StoreGetResponseDto>> getAllStores() {
    List<StoreGetResponseDto> stores = storeService.getAllStores();
    return ResponseEntity.ok(stores);
  }

  @PutMapping("/{storeId}")
  public ResponseEntity<?> updateStore(
      @PathVariable UUID storeId,
      @RequestBody StorePutRequestDto storeUpdateRequestDto) {

    storeService.updateStore(storeId, storeUpdateRequestDto);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{storeId}")
  public ResponseEntity<?> deleteStore(@PathVariable UUID storeId) {
    storeService.deleteStore(storeId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/search")
  public ResponseEntity<List<StoreGetResponseDto>> searchStores(@RequestParam String keyword) {
    List<StoreGetResponseDto> stores = storeService.searchStores(keyword);
    return ResponseEntity.ok(stores);
  }
}
