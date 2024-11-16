package com.sparta.peopleoff.domain.review.repository;

import com.sparta.peopleoff.domain.review.entity.ReviewEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {

}
