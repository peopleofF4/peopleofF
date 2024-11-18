package com.sparta.peopleoff.domain.review.repository;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.domain.review.entity.ReviewEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {

  Page<ReviewEntity> findByStoreIdAndDeletionStatus(UUID storeId, DeletionStatus status,
      Pageable pageable);

  Page<ReviewEntity> findByUserIdAndDeletionStatus(Long user_id, DeletionStatus deletionStatus,
      Pageable pageable);

  Page<ReviewEntity> findByCommentContainingAndDeletionStatus(
      String keyword, DeletionStatus deletionStatus, Pageable pageable);
}
