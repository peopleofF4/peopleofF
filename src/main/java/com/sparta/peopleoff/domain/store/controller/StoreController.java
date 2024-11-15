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
import org.springframework.security.access.prepost.PreAuthorize;
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
  @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
  public ResponseEntity<ApiResponse<Void>> registerStore(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody StorePostRequestDto storeRequestDto) {

    storeService.registerStore(storeRequestDto, userDetails.getUser());
    return ResponseEntity.status(ResBasicCode.CREATED.getHttpStatusCode())
        .body(ApiResponse.OK(ResBasicCode.CREATED));
  }

  @GetMapping("/stores/{storeId}")
  public ResponseEntity<ApiResponse<StoreGetResponseDto>> getStoreById(@PathVariable UUID storeId) {
    StoreGetResponseDto store = storeService.getStoreById(storeId);
    return ResponseEntity.ok(ApiResponse.OK(store, ResBasicCode.OK));
  }

  @GetMapping("/stores")
  public ResponseEntity<ApiResponse<List<StoreGetResponseDto>>> getAllStores(
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "0") int page) {

    pageSize = (pageSize == 10 || pageSize == 30 || pageSize == 50) ? pageSize : 10;

    List<StoreGetResponseDto> stores = storeService.getAllStores(sortBy, sortDirection, pageSize,
        page);
    return ResponseEntity.ok(ApiResponse.OK(stores, ResBasicCode.OK));
  }

  @PutMapping("/stores/{storeId}")
  @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
  public ResponseEntity<ApiResponse<Void>> updateStore(
      @PathVariable UUID storeId,
      @RequestBody StorePutRequestDto storeUpdateRequestDto) {

    storeService.updateStore(storeId, storeUpdateRequestDto);
    return ResponseEntity.ok(ApiResponse.OK(ResBasicCode.OK));
  }

  @DeleteMapping("/stores/{storeId}")
  @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
  public ResponseEntity<ApiResponse<Void>> deleteStore(
      @PathVariable UUID storeId) {

    storeService.deleteStore(storeId);
    return ResponseEntity.ok(ApiResponse.OK(ResBasicCode.OK));
  }

  @GetMapping("/stores/search")
  public ResponseEntity<ApiResponse<List<StoreGetResponseDto>>> searchStores(
      @RequestParam String keyword,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "0") int page) {

    pageSize = (pageSize == 10 || pageSize == 30 || pageSize == 50) ? pageSize : 10;
    List<StoreGetResponseDto> stores = storeService.searchStores(keyword, sortBy, sortDirection,
        pageSize, page);
    return ResponseEntity.ok(ApiResponse.OK(stores, ResBasicCode.OK));
  }
}