package com.sparta.peopleoff.common.auditing;

import com.sparta.peopleoff.common.entity.SoftDeleteEntity;
import com.sparta.peopleoff.common.enums.DeletionStatus;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;

@RequiredArgsConstructor
public class SoftDeleteEntityListener {

  private final AuditorAware<String> auditorAware;

  // Entity가 update 되었을때 호출
  @PreUpdate
  public void softDeleteHandle(SoftDeleteEntity softDeleteEntity) {

    // deletedStatus가 DELETED로 변경되었을 때, deletedBy와 deletedAt을 설정한다.
    if (softDeleteEntity.getDeletionStatus() == DeletionStatus.DELETED
        && softDeleteEntity.getDeletedAt() == null) {

      auditorAware.getCurrentAuditor().ifPresent(softDeleteEntity::setDeletedBy);
      softDeleteEntity.setDeletedAt(LocalDateTime.now());
    }


  }


}
