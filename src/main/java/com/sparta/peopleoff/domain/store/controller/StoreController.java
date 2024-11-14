package com.sparta.peopleoff.domain.store.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.store.dto.StoreGetResponseDto;
import com.sparta.peopleoff.domain.store.dto.StorePostRequestDto;
import com.sparta.peopleoff.domain.store.dto.StorePutRequestDto;
import com.sparta.peopleoff.domain.store.service.StoreService;
import com.sparta.peopleoff.security.UserDetailsImpl;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api/v1")
public class StoreController {

  private final StoreService storeService;

  public StoreController(StoreService storeService) {
    this.storeService = storeService;
  }

  @PostMapping("/stores")
  public ResponseEntity<ApiResponse<Void>> registerStore(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody StorePostRequestDto storeRequestDto) {

    // 권한 체크: OWNER만 접근 가능

    storeService.registerStore(storeRequestDto, userDetails.getUser());
    return ResponseEntity.status(ResBasicCode.CREATED.getHttpStatusCode())
        .body(ApiResponse.OK(ResBasicCode.CREATED));
  }

  @GetMapping("/stores/{storeId}")
  public ResponseEntity<ApiResponse<StoreGetResponseDto>> getStoreById(@PathVariable UUID storeId) {
    StoreGetResponseDto store = storeService.getStoreById(storeId);
    return ResponseEntity.ok(ApiResponse.OK(store, ResBasicCode.OK));
  }

  @GetMapping("/stores/stores")
  public ResponseEntity<ApiResponse<List<StoreGetResponseDto>>> getAllStores() {
    List<StoreGetResponseDto> stores = storeService.getAllStores();
    return ResponseEntity.ok(ApiResponse.OK(stores, ResBasicCode.OK));
  }

  @PutMapping("/stores/{storeId}")
  public ResponseEntity<ApiResponse<Void>> updateStore(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable UUID storeId,
      @RequestBody StorePutRequestDto storeUpdateRequestDto) {

    // 권한 체크: OWNER만 접근 가능

    storeService.updateStore(storeId, storeUpdateRequestDto);
    return ResponseEntity.ok(ApiResponse.OK(ResBasicCode.OK));
  }

  @DeleteMapping("/stores/{storeId}")
  public ResponseEntity<ApiResponse<Void>> deleteStore(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable UUID storeId) {

    // 권한 체크: OWNER만 접근 가능

    storeService.deleteStore(storeId);
    return ResponseEntity.ok(ApiResponse.OK(ResBasicCode.OK));
  }

  @GetMapping("/stores/search")
  public ResponseEntity<ApiResponse<List<StoreGetResponseDto>>> searchStores(
      @RequestParam String keyword) {
    List<StoreGetResponseDto> stores = storeService.searchStores(keyword);
    return ResponseEntity.ok(ApiResponse.OK(stores, ResBasicCode.OK));
  }
}
