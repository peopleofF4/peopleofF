package com.sparta.peopleoff.domain.review.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.common.util.PaginationUtils;
import com.sparta.peopleoff.domain.review.dto.ReviewGetResponseDto;
import com.sparta.peopleoff.domain.review.dto.ReviewPostRequestDto;
import com.sparta.peopleoff.domain.review.dto.ReviewPutRequestDto;
import com.sparta.peopleoff.domain.review.service.ReviewService;
import com.sparta.peopleoff.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
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
public class ReviewController {

  private final ReviewService reviewService;

  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  /**
   * 리뷰 등록
   *
   * @param requestDto
   * @param userDetails
   * @return
   */
  @PostMapping("/reviews")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<ApiResponse<Void>> registerReview(
      @Valid @RequestBody ReviewPostRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    reviewService.registerReview(requestDto, userDetails.getUser());
    return ResponseEntity.status(ResBasicCode.CREATED.getHttpStatusCode())
        .body(ApiResponse.OK(ResBasicCode.CREATED));
  }

  /**
   * 특정 가게의 리뷰 및 평점 전체 조회
   *
   * @param storeId
   * @param sortBy
   * @param sortDirection
   * @param pageSize
   * @param page
   * @return
   */
  @GetMapping("/stores/{storeId}/reviews")
  public ResponseEntity<ApiResponse<List<ReviewGetResponseDto>>> getReviewsByStore(
      @PathVariable UUID storeId,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "1") int page) {
    Pageable pageable = PaginationUtils.createPageable(sortBy, sortDirection, pageSize, page);
    List<ReviewGetResponseDto> reviews = reviewService.getReviewsByStore(storeId, pageable);
    return ResponseEntity.ok(ApiResponse.OK(reviews, ResBasicCode.OK));
  }

  /**
   * 특정 리뷰 조회
   *
   * @param reviewId
   * @return
   */
  @GetMapping("/reviews/{reviewId}")
  public ResponseEntity<ApiResponse<ReviewGetResponseDto>> getReviewById(
      @PathVariable UUID reviewId) {
    ReviewGetResponseDto review = reviewService.getReviewById(reviewId);
    return ResponseEntity.ok(ApiResponse.OK(review, ResBasicCode.OK));
  }

  /**
   * 특정 유저의 리뷰 및 평점 전체 조회
   *
   * @param userId
   * @param sortBy
   * @param sortDirection
   * @param pageSize
   * @param page
   * @return
   */
  @GetMapping("/users/{userId}/reviews")
  public ResponseEntity<ApiResponse<List<ReviewGetResponseDto>>> getReviewsByUser(
      @PathVariable Long userId,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "1") int page) {
    Pageable pageable = PaginationUtils.createPageable(sortBy, sortDirection, pageSize, page);
    List<ReviewGetResponseDto> reviews = reviewService.getReviewsByUser(userId, pageable);
    return ResponseEntity.ok(ApiResponse.OK(reviews, ResBasicCode.OK));
  }

  /**
   * 리뷰 수정
   *
   * @param reviewId
   * @param requestDto
   * @param userDetails
   * @return
   */
  @PutMapping("/reviews/{reviewId}")
  @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER', 'MASTER')")
  public ResponseEntity<ApiResponse<Void>> updateReview(
      @PathVariable UUID reviewId,
      @RequestBody @Valid ReviewPutRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    reviewService.updateReview(reviewId, requestDto, userDetails.getUser());
    return ResponseEntity.ok(ApiResponse.OK(ResBasicCode.OK));
  }

  /**
   * 리뷰 삭제 (soft-delete)
   *
   * @param reviewId
   * @return
   */
  @DeleteMapping("/reviews/{reviewId}")
  @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER', 'MASTER')")
  public ResponseEntity<ApiResponse<Void>> deleteReview(
      @PathVariable UUID reviewId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    reviewService.deleteReview(reviewId, userDetails.getUser());
    return ResponseEntity.ok(ApiResponse.OK(ResBasicCode.OK));
  }

  /**
   * 리뷰 검색
   *
   * @param keyword
   * @param sortBy
   * @param sortDirection
   * @param pageSize
   * @param page
   * @return
   */
  @GetMapping("/reviews/search")
  public ResponseEntity<ApiResponse<List<ReviewGetResponseDto>>> searchReviews(
      @RequestParam String keyword,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "1") int page) {

    Pageable pageable = PaginationUtils.createPageable(sortBy, sortDirection, pageSize, page);
    List<ReviewGetResponseDto> reviews = reviewService.searchReviews(keyword, pageable);
    return ResponseEntity.ok(ApiResponse.OK(reviews, ResBasicCode.OK));
  }
}
