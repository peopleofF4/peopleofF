package com.sparta.peopleoff.domain.store.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ForbiddenErrorCode;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.store.dto.StoreGetResponseDto;
import com.sparta.peopleoff.domain.store.dto.StorePostRequestDto;
import com.sparta.peopleoff.domain.store.dto.StorePutRequestDto;
import com.sparta.peopleoff.domain.store.service.StoreService;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.service.UserService;
import com.sparta.peopleoff.exception.CustomApiException;
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
  public ResponseEntity<ApiResponse<Void>> registerStore(
      @RequestBody StorePostRequestDto storeRequestDto) {

    // UserEntity user = userService.getAuthenticatedUser();

    // 테스트용
    UserEntity user = userService.findById(storeRequestDto.getUserId())
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "인증되지 않은 사용자 ID입니다."));

    if (!user.getRole().equals("OWNER")) {
      throw new CustomApiException(ForbiddenErrorCode.FORBIDDEN_OWNER, "사장님만 접근 가능합니다.");
    }

    storeService.registerStore(storeRequestDto, user);
    return ResponseEntity.ok(ApiResponse.OK(null));
  }

  @GetMapping("/{storeId}")
  public ResponseEntity<ApiResponse<StoreGetResponseDto>> getStoreById(@PathVariable UUID storeId) {
    StoreGetResponseDto store = storeService.getStoreById(storeId);
    return ResponseEntity.ok(ApiResponse.OK(store));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<StoreGetResponseDto>>> getAllStores() {
    List<StoreGetResponseDto> stores = storeService.getAllStores();
    return ResponseEntity.ok(ApiResponse.OK(stores));
  }

  @PutMapping("/{storeId}")
  public ResponseEntity<ApiResponse<Void>> updateStore(
      @PathVariable UUID storeId,
      @RequestBody StorePutRequestDto storeUpdateRequestDto) {

    storeService.updateStore(storeId, storeUpdateRequestDto);
    return ResponseEntity.ok(ApiResponse.OK(null));
  }

  @DeleteMapping("/{storeId}")
  public ResponseEntity<ApiResponse<Void>> deleteStore(@PathVariable UUID storeId) {
    storeService.deleteStore(storeId);
    return ResponseEntity.ok(ApiResponse.OK(null));
  }

  @GetMapping("/search")
  public ResponseEntity<ApiResponse<List<StoreGetResponseDto>>> searchStores(
      @RequestParam String keyword) {
    List<StoreGetResponseDto> stores = storeService.searchStores(keyword);
    return ResponseEntity.ok(ApiResponse.OK(stores));
  }
}
