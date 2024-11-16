package com.sparta.peopleoff.domain.review.service;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.common.rescode.ResErrorCode;
import com.sparta.peopleoff.domain.order.entity.OrderEntity;
import com.sparta.peopleoff.domain.order.repository.OrderRepository;
import com.sparta.peopleoff.domain.review.dto.ReviewGetResponseDto;
import com.sparta.peopleoff.domain.review.dto.ReviewPostRequestDto;
import com.sparta.peopleoff.domain.review.entity.ReviewEntity;
import com.sparta.peopleoff.domain.review.repository.ReviewRepository;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
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
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "유효하지 않은 주문 ID입니다."));

    if (!order.getUser().getId().equals(user.getId())) {
      throw new CustomApiException(ResErrorCode.REVIEW_UNAUTHORIZED, "Unauthorized access");
    }

    StoreEntity store = order.getStore();
    if (store == null) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "유효하지 않은 스토어입니다.");
    }

    ReviewEntity review = new ReviewEntity(
        requestDto.getComment(),
        requestDto.getRating(),
        store,
        user,
        order
    );

    reviewRepository.save(review);
    store.updateRating(requestDto.getRating());
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
  @Override
  @Transactional(readOnly = true)
  public List<ReviewGetResponseDto> getReviewsByUser(Long userId, Pageable pageable) {
    Page<ReviewEntity> reviews = reviewRepository.findByUserIdAndDeletionStatus(userId,
        DeletionStatus.ACTIVE, pageable);
    return reviews.stream()
        .map(ReviewGetResponseDto::new)
        .collect(Collectors.toList());
  }
}

