package com.sparta.peopleoff.domain.review.entity;

import com.sparta.peopleoff.common.entity.SoftDeleteEntity;
import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.domain.order.entity.OrderEntity;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_review")
public class ReviewEntity extends SoftDeleteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 500)
  private String comment;

  @Column(nullable = false)
  private int rating;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id", nullable = false)
  private StoreEntity store;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @OneToOne
  @JoinColumn(name = "order_id", nullable = false)
  private OrderEntity order;

  public ReviewEntity(String comment, int rating, StoreEntity store, UserEntity user,
      OrderEntity order) {
    this.comment = comment;
    this.rating = rating;
    this.store = store;
    this.user = user;
    this.order = order;
  }

  public void delete() {
    this.setDeletionStatus(DeletionStatus.DELETED);
  }

  public void update(@NotBlank String comment, @NotNull @Min(1) @Max(5) int rating) {
    this.comment = comment;
    this.rating = rating;
  }
}