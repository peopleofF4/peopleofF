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

  /**
   * 가게 등록
   *
   * @param userDetails
   * @param storeRequestDto
   * @return
   */
  @PostMapping("/stores")
  @PreAuthorize("hasAnyRole('OWNER')")
  public ResponseEntity<ApiResponse<Void>> registerStore(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody StorePostRequestDto storeRequestDto) {

    storeService.registerStore(storeRequestDto, userDetails.getUser());
    return ResponseEntity.status(ResBasicCode.CREATED.getHttpStatusCode())
        .body(ApiResponse.OK(ResBasicCode.CREATED));
  }

  /**
   * 가게 단건 조회
   *
   * @param storeId
   * @return
   */
  @GetMapping("/stores/{storeId}")
  public ResponseEntity<ApiResponse<StoreGetResponseDto>> getStoreById(@PathVariable UUID storeId) {
    StoreGetResponseDto store = storeService.getStoreById(storeId);
    return ResponseEntity.ok(ApiResponse.OK(store, ResBasicCode.OK));
  }

  /**
   * 가게 전체 조회
   *
   * @param sortBy
   * @param sortDirection
   * @param pageSize
   * @param page
   * @return
   */
  @GetMapping("/stores")
  public ResponseEntity<ApiResponse<List<StoreGetResponseDto>>> getAllStores(
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "0") int page) {

    pageSize = validatePageSize(pageSize);

    List<StoreGetResponseDto> stores = storeService.getAllStores(sortBy, sortDirection, pageSize,
        page);
    return ResponseEntity.ok(ApiResponse.OK(stores, ResBasicCode.OK));
  }

  /**
   * 사장님 본인 가게 조회
   *
   * @param userDetails
   * @return
   */
  @GetMapping("/stores/my")
  @PreAuthorize("hasRole('OWNER')")
  public ResponseEntity<ApiResponse<List<StoreGetResponseDto>>> getMyStores(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    List<StoreGetResponseDto> stores = storeService.getStoresByOwner(userDetails.getUser());
    return ResponseEntity.ok(ApiResponse.OK(stores, ResBasicCode.OK));
  }

  /**
   * 가게 수정
   *
   * @param storeId
   * @param storeUpdateRequestDto
   * @return
   */
  @PutMapping("/stores/{storeId}")
  @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
  public ResponseEntity<ApiResponse<Void>> updateStore(
      @PathVariable UUID storeId,
      @RequestBody StorePutRequestDto storeUpdateRequestDto) {

    storeService.updateStore(storeId, storeUpdateRequestDto);
    return ResponseEntity.ok(ApiResponse.OK(ResBasicCode.OK));
  }

  /**
   * 가게 삭제 (soft-delete)
   *
   * @param storeId
   * @return
   */
  @DeleteMapping("/stores/{storeId}")
  @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
  public ResponseEntity<ApiResponse<Void>> deleteStore(
      @PathVariable UUID storeId) {

    storeService.deleteStore(storeId);
    return ResponseEntity.ok(ApiResponse.OK(ResBasicCode.OK));
  }

  /**
   * 가게 검색
   *
   * @param keyword
   * @param sortBy
   * @param sortDirection
   * @param pageSize
   * @param page
   * @return
   */
  @GetMapping("/stores/search")
  public ResponseEntity<ApiResponse<List<StoreGetResponseDto>>> searchStores(
      @RequestParam String keyword,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "0") int page) {

    pageSize = validatePageSize(pageSize);

    List<StoreGetResponseDto> stores = storeService.searchStores(keyword, sortBy, sortDirection,
        pageSize, page);
    return ResponseEntity.ok(ApiResponse.OK(stores, ResBasicCode.OK));
  }

  /**
   * 가게별 리뷰 평점 평균값 반환
   *
   * @param storeId
   * @return
   */
  @GetMapping("/stores/{storeId}/reviews/avg")
  public ResponseEntity<ApiResponse<Double>> getAverageRating(@PathVariable UUID storeId) {
    double averageRating = storeService.getAverageRating(storeId);
    return ResponseEntity.ok(ApiResponse.OK(averageRating, ResBasicCode.OK));
  }


  private int validatePageSize(int pageSize) {
    return (pageSize == 10 || pageSize == 30 || pageSize == 50) ? pageSize : 10;
  }
}