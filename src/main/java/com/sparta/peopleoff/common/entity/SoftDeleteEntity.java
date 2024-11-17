package com.sparta.peopleoff.common.entity;

import com.sparta.peopleoff.common.auditing.SoftDeleteEntityListener;
import com.sparta.peopleoff.common.enums.DeletionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(SoftDeleteEntityListener.class)
public abstract class SoftDeleteEntity extends BaseEntity {

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "deleted_by")
  private String deletedBy;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private DeletionStatus deletionStatus = DeletionStatus.ACTIVE;

}
