package com.sparta.peopleoff.domain.review.service;

import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.common.rescode.ResErrorCode;
import com.sparta.peopleoff.domain.order.entity.OrderEntity;
import com.sparta.peopleoff.domain.order.repository.OrderRepository;
import com.sparta.peopleoff.domain.review.dto.ReviewPostRequestDto;
import com.sparta.peopleoff.domain.review.entity.ReviewEntity;
import com.sparta.peopleoff.domain.review.repository.ReviewRepository;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.exception.CustomApiException;
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
}

