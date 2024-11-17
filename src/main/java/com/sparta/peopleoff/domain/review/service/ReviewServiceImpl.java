package com.sparta.peopleoff.domain.review.service;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.common.rescode.ResErrorCode;
import com.sparta.peopleoff.common.util.DeletionValidator;
import com.sparta.peopleoff.domain.order.entity.OrderEntity;
import com.sparta.peopleoff.domain.order.repository.OrderRepository;
import com.sparta.peopleoff.domain.review.dto.ReviewGetResponseDto;
import com.sparta.peopleoff.domain.review.dto.ReviewPostRequestDto;
import com.sparta.peopleoff.domain.review.dto.ReviewPutRequestDto;
import com.sparta.peopleoff.domain.review.entity.ReviewEntity;
import com.sparta.peopleoff.domain.review.repository.ReviewRepository;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.entity.enums.UserRole;
import com.sparta.peopleoff.exception.CustomApiException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository reviewRepository;
  private final OrderRepository orderRepository;

  public ReviewServiceImpl(ReviewRepository reviewRepository, OrderRepository orderRepository) {
    this.reviewRepository = reviewRepository;
    this.orderRepository = orderRepository;
  }

  /**
   * 리뷰 등록
   *
   * @param requestDto
   * @param user
   */
  @Override
  @Transactional
  public void registerReview(ReviewPostRequestDto requestDto, UserEntity user) {
    OrderEntity order = orderRepository.findById(requestDto.getOrderId())
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "주문 내역이 존재하지 않습니다."));
    DeletionValidator.validateActive(order.getDeletionStatus(), "주문 내역");

    if (user.getRole() == UserRole.CUSTOMER && !order.getUser().getId().equals(user.getId())) {
      throw new CustomApiException(ResErrorCode.REVIEW_UNAUTHORIZED, "해당 건 주문자만 작성 가능합니다.");
    }

    StoreEntity store = order.getStore();
    if (store == null) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "해당 가게가 존재하지 않습니다.");
    }
    DeletionValidator.validateActive(store.getDeletionStatus(), "가게");

    ReviewEntity review = new ReviewEntity(
        requestDto.getComment(),
        requestDto.getRating(),
        store,
        user,
        order
    );

    reviewRepository.save(review);
    store.addRating(requestDto.getRating());
  }


  /**
   * 특정 리뷰 조회
   *
   * @param reviewId
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public ReviewGetResponseDto getReviewById(UUID reviewId) {
    ReviewEntity review = findActiveReviewById(reviewId);
    return new ReviewGetResponseDto(review);
  }

  /**
   * 특정 가게의 리뷰 및 평점 전체 조회
   *
   * @param storeId
   * @param pageable
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public List<ReviewGetResponseDto> getReviewsByStore(UUID storeId, Pageable pageable) {
    Page<ReviewEntity> reviews = reviewRepository.findByStoreIdAndDeletionStatus(storeId,
        DeletionStatus.ACTIVE, pageable);
    return reviews.stream()
        .map(ReviewGetResponseDto::new)
        .collect(Collectors.toList());
  }

  /**
   * 특정 유저의 리뷰 및 평점 전체 조회
   *
   * @param userId
   * @param pageable
   * @return
   */
  @Transactional(readOnly = true)
  @Override
  public List<ReviewGetResponseDto> getReviewsByUser(Long userId, Pageable pageable) {
    Page<ReviewEntity> reviews = reviewRepository.findByUserIdAndDeletionStatus(userId,
        DeletionStatus.ACTIVE, pageable);
    return reviews.stream()
        .map(ReviewGetResponseDto::new)
        .collect(Collectors.toList());
  }

  /**
   * 리뷰 수정
   *
   * @param reviewId
   * @param requestDto
   * @param user
   */
  @Transactional
  @Override
  public void updateReview(UUID reviewId, ReviewPutRequestDto requestDto, UserEntity user) {
    ReviewEntity review = findActiveReviewById(reviewId);

    if (user.getRole() == UserRole.CUSTOMER && !review.getUser().getId().equals(user.getId())) {
      throw new CustomApiException(ResErrorCode.REVIEW_UNAUTHORIZED, "작성자 본인만 수정 가능합니다.");
    }

    int previousRating = review.getRating(); // 기존 평점 저장
    review.update(requestDto.getComment(), requestDto.getRating());

    review.getStore().updateRating(previousRating, requestDto.getRating());
  }

  /**
   * 리뷰 삭제 (soft-delete)
   *
   * @param reviewId
   */
  @Transactional
  @Override
  public void deleteReview(UUID reviewId, UserEntity user) {
    ReviewEntity review = findActiveReviewById(reviewId);

    if (user.getRole() == UserRole.CUSTOMER && !review.getUser().getId().equals(user.getId())) {
      throw new CustomApiException(ResErrorCode.REVIEW_UNAUTHORIZED, "작성자 본인만 수정 가능합니다.");
    }

    review.getStore().removeRating(review.getRating());
    review.delete();
  }

  /**
   * 리뷰 검색
   *
   * @param keyword
   * @param pageable
   * @return
   */
  @Transactional(readOnly = true)
  @Override
  public List<ReviewGetResponseDto> searchReviews(String keyword, Pageable pageable) {

    Page<ReviewEntity> reviewPage = reviewRepository.findByCommentContainingAndDeletionStatus(
        keyword, DeletionStatus.ACTIVE, pageable);

    return reviewPage.stream()
        .map(ReviewGetResponseDto::new)
        .collect(Collectors.toList());
  }

  private ReviewEntity findActiveReviewById(UUID reviewId) {
    ReviewEntity review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "해당 리뷰가 존재하지 않습니다."));
    DeletionValidator.validateActive(review.getDeletionStatus(), "리뷰");
    return review;
  }
}

